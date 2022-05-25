package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.AnswerTest.A1;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;

    @Test
    void 저장_테스트() {
        Answer expected = A1;
        Answer result = answerRepository.save(expected);

        assertThat(result.getId()).isNotNull();
    }

    @Test
    void 아이디로_조회() {
        Answer save = answerRepository.save(A1);
        Answer expected = answerRepository.findByIdAndDeletedFalse(save.getId()).get();
        assertThat(expected.getId()).isEqualTo(save.getId());
    }

}