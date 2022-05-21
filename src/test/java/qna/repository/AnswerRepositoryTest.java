package qna.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;

import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("답변 등록 테스트")
    void save() {
        answerRepository.save(A1);
        answerRepository.save(A2);

        Assertions.assertThat(answerRepository.findAll()).size().isEqualTo(2);
    }

    @Test
    @DisplayName("답변 등록 후 조회 테스트")
    void find() {
        answerRepository.save(A1);

        Answer answer = answerRepository.findByIdAndDeletedFalse(A1.getId()).get();

        Assertions.assertThat(answer).isNotNull();
        Assertions.assertThat(answer.getId()).isEqualTo(A1.getId());
        Assertions.assertThat(answer.isDeleted()).isFalse();
    }

}
