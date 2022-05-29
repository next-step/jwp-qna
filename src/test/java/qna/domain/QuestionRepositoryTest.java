package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    private User savedWriter;
    private Question savedQuestion;

    @BeforeEach
    void setUp() {
        savedWriter = userRepository.save(new User("user", "1234", "username", "test@gmail.com"));
        savedQuestion = questionRepository.save(new Question("question title", "question content").writeBy(savedWriter));
    }

    @AfterEach
    void clear() {
        questionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("질문을 저장한다.")
    void save() {
        assertThat(savedQuestion)
                .isNotNull()
                .satisfies(question -> {
                    assertThat(question.getTitle())
                            .isEqualTo("question title");
                    assertThat(question.getContents())
                            .isEqualTo("question content");
                    assertThat(question.getWriter())
                            .isEqualTo(savedWriter);
                });
    }

    @Test
    @DisplayName("질문 Id로 조회한다.")
    void findByIdAndDeletedFalse() {
        Optional<Question> foundQuestion = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId());

        assertThat(foundQuestion)
                .isNotEmpty()
                .hasValueSatisfying(question -> assertThat(question).isEqualTo(savedQuestion));
    }

    @Test
    @DisplayName("저장된 전체 질문들을 조회한다.")
    void findByDeletedFalse() {
        Question savedQuestion2 = questionRepository.save(new Question("question2 title", "question2 content").writeBy(savedWriter));
        Question savedQuestion3 = questionRepository.save(new Question("question3 title", "question3 content").writeBy(savedWriter));

        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions)
                .containsExactly(savedQuestion, savedQuestion2, savedQuestion3);
    }
}
