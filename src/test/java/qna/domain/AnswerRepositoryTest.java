package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
                () -> assertEquals(UserTest.JAVAJIGI.getId(), answer.getWriterId()),
                () -> assertEquals(QuestionTest.Q1.getId(), answer.getQuestionId())
        );
    }

    @Test
    @DisplayName("Answer Find By Question id And Deleted False Test")
    void answer_repository_findByQuestionIdAndDeletedFalse_return() {
        Answer answer = answerRepository.save(AnswerTest.A1);
        List<Answer> answerList = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());
        assertAll(
                () -> assertThat(answerList.size()).isEqualTo(1),
                () -> assertTrue(answerList.contains(answer))
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