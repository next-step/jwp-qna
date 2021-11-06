package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@EnableJpaAuditing
@DisplayName("AnswerRepository 테스트")
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    private Question question;
    private Answer answer;
    private Answer answer2;

    @BeforeEach
    void setUp() {
        User user = new User("test_id", "Passw0rd!", "홍길동", "test@email.com");
        question = new Question("질문", "질문 내용");
        answer = new Answer(user, question, "답변 내용");
        answer2 = new Answer(user, question, "답변2 내용");
        answer2.setDeleted(true);
    }

    @Test
    @DisplayName("Answer를 저장한다.")
    void save() {
        // when
        Answer savedAnswer = answerRepository.save(answer);

        // then
        assertAll(
                () -> assertThat(savedAnswer.getId()).isNotNull(),
                () -> assertThat(savedAnswer.getCreatedAt()).isNotNull(),
                () -> assertThat(savedAnswer.getUpdatedAt()).isNotNull(),
                () -> assertThat(savedAnswer).isEqualTo(answer)
        );
    }

    @Test
    @DisplayName("questionId와 deleted로 Answer 리스트를 조회한다.")
    void findByQuestionIdAndDeletedFalse() {
        // given
        answerRepository.save(answer);
        answerRepository.save(answer2);

        // when
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        // then
        assertThat(answers).containsExactly(answer);
    }

    @Test
    @DisplayName("id와 deleted로 Answer를 조회한다.")
    void findByIdAndDeletedFalse() {
        // given
        Answer savedAnswer = answerRepository.save(this.answer);

        // when
        Optional<Answer> answerOptional = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());

        // then
        assertAll(
                () -> assertThat(answerOptional.isPresent()).isTrue(),
                () -> assertThat(answerOptional.get()).isEqualTo(answer)
        );
    }
}
