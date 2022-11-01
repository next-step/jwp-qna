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
    @DisplayName("Answer 저장 테스트")
    void save() {
        Answer answer = answerRepository.save(answerTest);
        assertThat(answer.getId()).isNotNull();
    }


}
