package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;
    
    @BeforeEach
    void setUp() {
        answerRepository.deleteAllInBatch();
    }
    
    @Test
    void save() {
        Answer answerTest = AnswerTest.A1;
        Answer answer = answerRepository.save(answerTest);
        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(answer.getContents()).isEqualTo(answerTest.getContents()),
                () -> assertThat(answer.getWriterId()).isEqualTo(answerTest.getWriterId()),
                () -> assertThat(answer.getQuestionId()).isEqualTo(answerTest.getQuestionId()),
                () -> assertThat(answer.isDeleted()).isEqualTo(answerTest.isDeleted())
        );
    }
    
}
