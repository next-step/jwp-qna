package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AnswersTest {

    @Test
    @DisplayName("답변 목록 추가 확인")
    void 답변_목록_추가_확인() {
        // Given
        Answers answers = new Answers();
        
        // When
        answers.add(new Answer(UserTest.JENNIE, QuestionTest.Q1, "이렇게 하시면 됩니다~"));
        answers.add(new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "이렇게 하시면 됩니다~2"));
        answers.add(new Answer(UserTest.SANJIGI, QuestionTest.Q1, "이렇게 하시면 됩니다~3"));
        
        // Then
        assertThat(answers.getAnswers()).hasSize(3);
    }
    
    @Test
    @DisplayName("답변 목록 삭제 확인")
    void 답변_목록_삭제_확인() {
        // Given
        Answers answers = new Answers();
        
        // When
        answers.add(new Answer(UserTest.JENNIE, QuestionTest.Q1, "이렇게 하시면 됩니다~"));
        answers.add(new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "이렇게 하시면 됩니다~2"));
        answers.add(new Answer(UserTest.SANJIGI, QuestionTest.Q1, "이렇게 하시면 됩니다~3"));
        
        // Then
        assertThat(answers.delete().countDeleteHistories()).isEqualTo(3);
    }
}
