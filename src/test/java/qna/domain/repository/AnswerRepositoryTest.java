package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.AnswerTest;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;
    private Answer savedAnswer1, savedAnswer2;
    
    @BeforeEach
    void saveAnswer() {
        savedAnswer1 = answerRepository.save(AnswerTest.A1);
        savedAnswer2 = answerRepository.save(AnswerTest.A2);
    }
    
    @Test
    @DisplayName("저장")
    void saveTest() {
        assertAll(
            () -> assertThat(answerRepository.count()).isEqualTo(2),
            () -> assertThat(AnswerTest.A1.getContents()).isEqualTo(savedAnswer1.getContents()),
            () -> assertThat(AnswerTest.A2.getQuestionId()).isEqualTo(savedAnswer2.getQuestionId())
        );
    }
    
    @Test
    @DisplayName("수정")
    void updateTest() {
        savedAnswer1.setContents("updated_contents");
        Answer selectedAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer1.getId()).get();
        assertThat(selectedAnswer.getContents()).isEqualTo(savedAnswer1.getContents());
    }
    
    @Test
    @DisplayName("삭제")
    void deleteTest() {
        answerRepository.delete(savedAnswer1);
        assertAll(
            () -> assertThat(answerRepository.count()).isEqualTo(1),
            () -> assertThat(answerRepository.existsById(savedAnswer1.getId())).isFalse()
        );
    }
    
    @Test
    @DisplayName("Question ID로 찾기")
    void findByQuestionIdAndDeletedFalse() {
        assertThat(answerRepository.findByQuestionIdAndDeletedFalse(AnswerTest.A1.getQuestionId())).contains(savedAnswer1);
    }
    
}
