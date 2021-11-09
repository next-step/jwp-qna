package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EnableJpaAuditing
@DisplayName("QuestionRepository 테스트")
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    private Question question;
    private Question deletedQuestion;

    @BeforeEach
    void setUp() {
        User user = new User("test_id", "Passw0rd!", "홍길동", "test@email.com");
        question = new Question("질문", "질문 내용");
        deletedQuestion = new Question("질문2", "질문2 내용").writeBy(user);
        deletedQuestion.delete(user);
    }

    @Test
    @DisplayName("Question를 저장한다.")
    void save() {
        // when
        Question savedQuestion = questionRepository.save(question);

        // then
        assertAll(
                () -> assertThat(savedQuestion.getId()).isNotNull(),
                () -> assertThat(savedQuestion.getCreatedAt()).isNotNull(),
                () -> assertThat(savedQuestion.getUpdatedAt()).isNotNull(),
                () -> assertThat(savedQuestion).isEqualTo(question)
        );
    }

    @Test
    @DisplayName("삭제되지 않은 Question 리스트를 조회한다.")
    void findByDeletedFalse() {
        // given
        questionRepository.save(question);
        questionRepository.save(deletedQuestion);

        // when
        List<Question> questions = questionRepository.findByDeletedFalse();

        // then
        assertThat(questions).containsExactly(question);
    }

    @Test
    @DisplayName("id로 삭제되지 않은 Question을 조회한다.")
    void findByIdAndDeletedFalse() {
        // given
        Question savedQuestion = questionRepository.save(question);

        // when
        Optional<Question> questionOptional = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId());

        // then
        assertAll(
                () -> assertThat(questionOptional.isPresent()).isTrue(),
                () -> assertThat(questionOptional.get()).isEqualTo(question)
        );
    }
}
