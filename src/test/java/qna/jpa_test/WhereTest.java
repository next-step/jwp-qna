package qna.jpa_test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.AnswerTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class WhereTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("soft-delete 학습 테스트")
    void createdDateTest() {
        Answer actual = answerRepository.save(AnswerTest.A1);
        assertThat(actual.isDeleted()).isEqualTo(false);
        actual.setDeleted(true);

        List<Answer> result = answerRepository.findAll();
        assertThat(result.size()).isEqualTo(0);
    }

}
