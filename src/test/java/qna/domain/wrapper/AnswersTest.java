package qna.domain.wrapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.domain.Answer;
import qna.domain.QuestionTest;
import qna.domain.UserTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnswersTest {

    @DisplayName("question에 동일한 answer가 추가되면 예외가 발생한다")
    @Test
    void 동일_답변_추가시_예외_발생() {
        // given
        Answer answer = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "testContents");
        Answers answers = new Answers();
        answers.add(answer);

        // when & then
        assertThatThrownBy(() -> answers.add(answer))
                .isInstanceOf(IllegalArgumentException.class);
    }
}