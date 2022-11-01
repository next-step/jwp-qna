package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;

    public Answer answerTest;

    @BeforeEach
    void setUp() {
        answerTest = AnswerTest.A1;
    }

    @Test
    @DisplayName("Answer 저장한 엔티티의 id로 조회한 경우 동일성 테스트")
    void find() {
        Answer saveAnswer = answerRepository.save(answerTest);
        Answer findAnswer = answerRepository.findById(saveAnswer.getId()).orElse(null);
        assertThat(saveAnswer).isEqualTo(findAnswer);
    }

}
