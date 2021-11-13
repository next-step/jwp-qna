package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.exception.ExceptionMessage.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.exception.CannotDeleteException;

class AnswersTest {

    @DisplayName("질문자와 답변들의 답변자가 같은경우 예외가 발생하지 않는다")
    @Test
    void validateAnswersOwner() {
        // given
        User questionWriter = UserTest.JAVAJIGI;
        Question question = createQuestion("question1", "questionContents1", questionWriter);
        Answers answers = new Answers(Arrays.asList(
            createAnswer(questionWriter, question),
            createAnswer(questionWriter, question)
        ));

        // when && then
        assertDoesNotThrow(() -> answers.validateAnswersOwner(questionWriter));
    }

    @DisplayName("질문자와 답변들의 답변자가 다른경우 예외가 발생한다")
    @Test
    void validateAnswersOwnerException() {
        // given
        User questionWriter = UserTest.JAVAJIGI;
        Question question = createQuestion("question1", "questionContents1", questionWriter);
        Answers answers = new Answers(Arrays.asList(
            createAnswer(questionWriter, question),
            createAnswer(UserTest.SANJIGI, question)
        ));

        // when & then
        assertThatThrownBy(() -> answers.validateAnswersOwner(questionWriter))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessage(CANNOT_DELETE_ANSWER_DIFFERENT_USER_MESSAGE.getMessage());
    }

    private Answer createAnswer(User writer, Question question) {
        return new Answer(writer, question, "contents");
    }

    private Question createQuestion(String title, String contents, User writer) {
        return new Question(title, contents).writeBy(writer);
    }
}
