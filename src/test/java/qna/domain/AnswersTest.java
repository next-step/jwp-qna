package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnswersTest {
    @DisplayName("답변자가 아닌 유저는 답변을 삭제할 수 없다")
    @Test
    void deleteQuestion() {
        Answers answers = new Answers();
        answers.add(new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents1"));
        answers.add(new Answer(UserTest.SANJIGI, QuestionTest.Q1, "contents2"));

        assertThatThrownBy(() -> answers.validateAnswer(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}
