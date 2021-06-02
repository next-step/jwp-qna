package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;

public class AnswersTest {

    @Test
    void 답변삭제시_답변자중에서_질문자와_다른인물이_있을경우_예외발생() {
        Answers answers = new Answers(AnswerTest.A2);

        assertThatThrownBy(() -> answers.deleteAnswers(AnswerTest.A1.getWriter()))
            .isInstanceOf(CannotDeleteException.class);
    }

}