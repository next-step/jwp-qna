package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    private Answer answer;

    @BeforeEach
    void setup() {
        answer = answerRepository.save(A1);
        answerRepository.save(A2);
    }

    @Test
    void findByIdAndDeletedFalse() {
        Optional<Answer> byId = answerRepository.findByIdAndDeletedFalse(A1.getId());
        Answer answer = byId.orElse(null);
        assertAll(
            () -> assertThat(byId).isNotEmpty(),
            () -> assertEquals(A1.getContents(), answer.getContents()),
            () -> assertEquals(A1.getWriterId(), answer.getWriterId()),
            () -> assertEquals(A1.getQuestionId(), answer.getQuestionId()),
            () -> assertThat(answer.isDeleted()).isFalse()
        );
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        List<Answer> answerList = answerRepository.findByQuestionIdAndDeletedFalse(answer.getQuestionId());
        assertEquals(2, answerList.size());
    }
}