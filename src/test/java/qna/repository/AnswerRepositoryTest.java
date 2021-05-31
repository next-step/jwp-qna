package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.AnswerTest;
import qna.repository.AnswerRepository;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @BeforeAll
    void setup() {
        answerRepository.save(AnswerTest.A1);
        answerRepository.save(AnswerTest.A2);
    }

    @Test
    @DisplayName("답변ID 와 삭제여부로 조회")
    void findByIdAndDeletedFalse() {
        Answer expected = AnswerTest.A1;
        Answer actual = answerRepository.findByIdAndDeletedFalse(expected.getId()).get();

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    @DisplayName("질문ID 와 삭제여부로 조회")
    void findByQuestionIdAndDeletedFalse() {
        Answer answer = answerRepository.findByIdAndDeletedFalse(AnswerTest.A1.getId()).get();

        assertThat(answerRepository.findByQuestionIdAndDeletedFalse(answer.getQuestionId())).hasSize(2);
    }

}
