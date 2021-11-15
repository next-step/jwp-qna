package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnswersTest {
    @DisplayName("답변자와 삭제하는 유저가 다른 경우 테스트")
    @Test
    void deleteQuestion() {
        Answers answers = new Answers();
        answers.add(new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents1"));
        answers.add(new Answer(UserTest.SANJIGI, QuestionTest.Q1, "contents2"));

        assertThatThrownBy(() -> answers.validateAnswer(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}