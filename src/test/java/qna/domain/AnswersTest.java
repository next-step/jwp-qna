package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

public class AnswersTest {
    @Test
    @DisplayName("Answers 삭제: 정상")
    void Answers_삭제() {
        Answers answers = new Answers(Arrays.asList(AnswerTest.A1));
        answers.deleteAnswers(AnswerTest.A1.getWriter());
        assertThat(AnswerTest.A1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("Answers 삭제: 작성자가 맞지않아 실패")
    void Answers_삭제_실패(){
        Answers answers = new Answers(Arrays.asList(AnswerTest.A1));
        assertThatThrownBy(() -> {
            answers.deleteAnswers(AnswerTest.A2.getWriter());
        });
    }
}
