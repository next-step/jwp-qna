package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerTest.A2;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("answer_id로 검색 테스트")
    void find_by_answer_id() {
        // given
        answerRepository.save(A2);
        // when
        Optional<Answer> answer = answerRepository.findById(A2.getId());
        // then
        assertThat(answer).contains(A2);
    }
}
