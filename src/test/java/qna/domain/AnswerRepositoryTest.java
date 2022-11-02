package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("질문 식별자로 삭제되지 않는 답변을 조회")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        Answer answer = answerRepository.save(AnswerTest.A1);

        List<Answer> result = answerRepository.findByQuestionIdAndDeletedFalse(answer.getQuestionId());

        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result).contains(answer),
            () -> assertThat(result.get(0)).isEqualTo(answer)
        );
    }

    @DisplayName("답변 식별자로 삭제되지 않는 답변을 조회")
    @Test
    void findByIdAndDeletedFalse() {
        Answer answer = answerRepository.save(AnswerTest.A1);

        Optional<Answer> result = answerRepository.findByIdAndDeletedFalse(answer.getId());

        assertAll(
            () -> assertThat(result.isPresent()).isTrue(),
            () -> assertThat(result.get()).isEqualTo(answer)
        );
    }
}
