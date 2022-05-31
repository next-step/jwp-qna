package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private User savedWriter;
    private Question savedQuestion;
    private Answer savedAnswer;

    @BeforeEach
    void setUp() {
        savedWriter = userRepository.save(JAVAJIGI);
        savedQuestion = questionRepository.save(new Question("question title", "question content").writeBy(savedWriter));
        savedAnswer = answerRepository.save(new Answer(savedWriter, savedQuestion, "answer contents"));
    }

    @AfterEach
    void clear() {
        answerRepository.deleteAll();
        questionRepository.deleteAll();
        answerRepository.deleteAll();
    }

    @Test
    @DisplayName("답변을 저장한다.")
    void save() {
        assertThat(savedAnswer)
                .satisfies(answer -> {
                    assertThat(answer.getWriter())
                            .isEqualTo(savedWriter);
                    assertThat(answer.getQuestion())
                            .isEqualTo(savedQuestion);
                    assertThat(answer.getContents())
                            .isEqualTo("answer contents");
                });
    }

    @Test
    @DisplayName("답변Id로 조회한다.")
    void findByIdAndDeletedFalse() {
        Optional<Answer> foundAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());

        assertThat(foundAnswer)
                .isNotEmpty()
                .hasValueSatisfying(answer -> assertThat(answer).isEqualTo(savedAnswer));
    }

}
