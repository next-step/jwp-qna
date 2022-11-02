package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void findByDeletedFalseTest() {
        Question q2 = Q2;
        q2.setDeleted(true);
        questionRepository.saveAll(Arrays.asList(Q1, q2));

        assertThat(questionRepository.findByDeletedFalse())
                .hasSize(1)
                .containsExactly(Q1);
        assertThat(questionRepository.findByIdAndDeletedFalse(q2.getId()))
                .isEmpty();
    }

}