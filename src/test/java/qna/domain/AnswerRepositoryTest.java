package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;

    @Test
    @DisplayName("Answer 저장")
    void answer_repository_save() {
        Answer answer = answerRepository.save(AnswerTest.A1);
        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(UserTest.JAVAJIGI.getId()).isEqualTo(answer.getWriterId()),
                () -> assertThat(QuestionTest.Q1.getId()).isEqualTo(answer.getQuestionId())
        );
    }

    @Test
    @DisplayName("Answer Find By Question id And Deleted False Test")
    void answer_repository_findByQuestionIdAndDeletedFalse_return() {
        Answer answer = answerRepository.save(AnswerTest.A1);
        List<Answer> answerList = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());
        assertAll(
                () -> assertThat(answerList).hasSize(1),
                () -> assertThat(answerList).contains(answer)
        );
    }

    @Test
    @DisplayName("Answer Find By Id And Deleted False Test")
    void answer_repository_findByIdAndDeletedFalse_return() {
        Answer answer = answerRepository.save(AnswerTest.A1);
        Optional<Answer> result = answerRepository.findByIdAndDeletedFalse(answer.getId());
        assertThat(result.get()).isEqualTo(answer);
    }
}