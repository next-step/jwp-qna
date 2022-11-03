package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.AnswerTest.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest(properties = {"spring.jpa.hibernate.ddl-auto=validate"})
class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;

    @Test
    void 답변_저장_및_찾기() {
        Answer actual = answerRepository.save(A1);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getCreatedAt()).isNotNull(),
            () -> assertThat(actual.getUpdatedAt()).isNotNull(),
            () -> assertThat(actual.getUpdatedAt()).isEqualTo(actual.getCreatedAt()),
            () -> assertThat(actual.isDeleted()).isFalse(),
            () -> assertThat(actual.getQuestionId()).isEqualTo(A1.getQuestionId()),
            () -> assertThat(actual.getWriterId()).isEqualTo(A1.getWriterId())
        );
        assertThat(answerRepository.findByIdAndDeletedFalse(actual.getId())).contains(actual);
    }

    @Test
    void 답변_삭제여부_변경() {
        Answer actual = answerRepository.save(A1);
        assertThat(answerRepository.findByIdAndDeletedFalse(actual.getId())).isNotEmpty();
        actual.setDeleted(true);
        assertThat(answerRepository.findByIdAndDeletedFalse(actual.getId())).isEmpty();
    }

    @Test
    void 삭제되지않은_질문에대한_답변들_찾기() {
        Answer savedA1 = answerRepository.save(A1);
        Answer savedA2 = answerRepository.save(A2);
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(A1.getQuestionId());
        assertThat(answers).hasSize(2);
        assertThat(answers).containsExactly(savedA1, savedA2);
    }
}