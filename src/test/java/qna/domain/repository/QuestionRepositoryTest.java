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
import qna.domain.Question;
import qna.domain.QuestionTest;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;
    private Question savedQuestion1, savedQuestion2;
    
    @BeforeEach
    void saveQuestion() {
        savedQuestion1 = questionRepository.save(QuestionTest.Q1);
        savedQuestion2 = questionRepository.save(QuestionTest.Q2);
    }
    
    @Test
    @DisplayName("저장")
    void saveTest() {
        assertAll(
            () -> assertThat(questionRepository.count()).isEqualTo(2),
            () -> assertThat(QuestionTest.Q1.getTitle()).isEqualTo(savedQuestion1.getTitle()),
            () -> assertThat(QuestionTest.Q2.getContents()).isEqualTo(savedQuestion2.getContents())
        );
    }
    
    @Test
    @DisplayName("수정")
    void updateTest() {
        savedQuestion1.setContents("updated_contents");
        Question selectedQuestion = questionRepository.findByIdAndDeletedFalse(savedQuestion1.getId()).get();
        assertThat(selectedQuestion.getContents()).isEqualTo(savedQuestion1.getContents());
    }
    
    @Test
    @DisplayName("삭제")
    void deleteTest() {
        questionRepository.delete(savedQuestion1);
        assertAll(
            () -> assertThat(questionRepository.count()).isEqualTo(1),
            () -> assertThat(questionRepository.existsById(savedQuestion1.getId())).isFalse()
        );
    }
    
    @Test
    @DisplayName("삭제되지 않은 Question 찾기")
    void findByQuestionIdAndDeletedFalse() {
        assertThat(questionRepository.findByDeletedFalse()).contains(savedQuestion1);
    }
    
}
