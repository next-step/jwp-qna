package qna.domain.question;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.question.QuestionTest.Q1;

@DataJpaTest
public class QuestionRepositoryTest {
    
    @Autowired
    QuestionRepository questionRepository;
    
    @Test
    @DisplayName("질문이 정상적으로 등록되있는지 테스트 한다")
    void saveQuestionTest(){
        Question question = questionRepository.save(Q1);
        assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(question.getId()).isEqualTo(Q1.getId()),
                () -> assertThat(question.getContents()).isEqualTo(Q1.getContents()),
                () -> assertThat(question.getWriterId()).isEqualTo(Q1.getWriterId()),
                () -> assertThat(question.getCreatedAt()).isEqualTo(Q1.getCreatedAt()),
                () -> assertThat(question.getUpdatedAt()).isEqualTo(Q1.getUpdatedAt())
        );
    }
    
    @Test
    @DisplayName("id로 삭제되지 않은 질문 조회를 테스트한다")
    void findByDeletedFalseTest(){
        Question question = questionRepository.save(Q1);
        Question getQuestion = questionRepository.findByIdAndDeletedFalse(question.getId())
                .orElseThrow(() -> new NotFoundException());
        assertAll(
                () -> assertThat(getQuestion).isNotNull(),
                () -> assertThat(getQuestion.getId()).isEqualTo(Q1.getId()),
                () -> assertThat(getQuestion.getContents()).isEqualTo(Q1.getContents()),
                () -> assertThat(getQuestion.getWriterId()).isEqualTo(Q1.getWriterId()),
                () -> assertThat(getQuestion.getCreatedAt()).isEqualTo(Q1.getCreatedAt()),
                () -> assertThat(getQuestion.getUpdatedAt()).isEqualTo(Q1.getUpdatedAt())
        );
    }
    
    @Test
    @DisplayName("질문의 삭제여부가 true로 변경되었는지 테스트한다")
    void IsDeleteChangeTest(){
        Question question = questionRepository.save(Q1);
        question.setDeleted(true);
        Long id = question.getId();
        Optional<Question> byIdAndDeletedFalse = questionRepository.findByIdAndDeletedFalse(id);
        assertAll(
                () -> assertThat(byIdAndDeletedFalse).isEmpty(),
                () -> assertThat(question.isDeleted()).isTrue()
        );

    }

    @Test
    @DisplayName("질문이 실제 삭제되었는지 테스트한다")
    void deleteByIdTest(){
        Question question = questionRepository.save(Q1);
        questionRepository.deleteById(question.getId());
        assertAll(
                () -> assertThat(questionRepository.findById(question.getId())).isEmpty(),
                () -> assertThat(questionRepository.findByIdAndDeletedFalse(question.getId())).isEmpty()
        );
    }
    
}
