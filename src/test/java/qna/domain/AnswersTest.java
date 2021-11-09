package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.*;

class AnswersTest {

    @Test
    @DisplayName("질문자와 답변자가 다른 답변이 존재하면 예외가 발생한다.")
    void validateAnswers() {
        // given
        Answers answers = new Answers();
        answers.add(AnswerTest.A1);
        answers.add(AnswerTest.A2);

        // when &  then
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> answers.validateAnswers(UserTest.JAVAJIGI))
                .withMessageMatching("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
