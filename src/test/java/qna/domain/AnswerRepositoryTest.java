package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void findByQuestionIdAndDeletedFalseTest() {
        answerRepository.save(A1);
        Assertions.assertThat(answerRepository.findByQuestionIdAndDeletedFalse(A1.getQuestionId()))
                .hasSize(1)
                .containsExactly(A1);
    }

    @Test
    void findByIdAndDeletedFalseTest() {
        answerRepository.save(A2);
        Answer expected = answerRepository.findByIdAndDeletedFalse(A2.getId())
                .orElse(null);
        Assertions.assertThat(expected)
                .isEqualTo(A2);
    }

}