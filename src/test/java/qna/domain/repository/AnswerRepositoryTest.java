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
    private Answer answer1, answer2;
    
    @BeforeEach
    void saveAnswer() {
        answer1 = answerRepository.save(AnswerTest.A1);
        answer2 = answerRepository.save(AnswerTest.A2);
    }
    
    @Test
    @DisplayName("저장")
    void saveTest() {
        assertAll(
            () -> assertThat(answerRepository.count()).isEqualTo(2),
            () -> assertThat(AnswerTest.A1.getContents()).isEqualTo(answer1.getContents()),
            () -> assertThat(AnswerTest.A2.getQuestionId()).isEqualTo(answer2.getQuestionId())
        );
    }
    
    @Test
    @DisplayName("수정")
    void updateTest() {
        answer1.setContents("updated_contents");
        Answer selectedAnswer = answerRepository.findByIdAndDeletedFalse(answer1.getId()).get();
        assertThat(selectedAnswer.getContents()).isEqualTo(answer1.getContents());
    }
    
    @Test
    @DisplayName("삭제")
    void deleteTest() {
        answerRepository.delete(answer1);
        assertAll(
            () -> assertThat(answerRepository.count()).isEqualTo(1),
            () -> assertThat(answerRepository.existsById(answer1.getId())).isFalse()
        );
    }
    
    @Test
    @DisplayName("Question ID로 찾기")
    void findByQuestionIdAndDeletedFalse() {
        assertThat(answerRepository.findByQuestionIdAndDeletedFalse(AnswerTest.A1.getQuestionId())).contains(answer1);
    }
    
}
