package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.QuestionTest.*;
import static qna.exception.ExceptionMessage.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.exception.CannotDeleteException;

class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, Q1, "Answers Contents2");

    @DisplayName("changeQuestion 을 여러번 호출하면 기존 question 의 answer 는 지워진다.")
    @Test
    void changeQuestion() {
        // given
        Question question1 = createQuestion("question1", "questionContents1");
        Question question2 = createQuestion("question2", "questionContents2");
        Answer answer = createAnswer(UserTest.JAVAJIGI, question1);

        // when
        answer.changeQuestion(question2);

        // then
        assertEquals(question2, answer.getQuestion());
        assertEquals(1, answer.getQuestion().getAnswers().size());
        assertEquals(0, question1.getAnswers().size());
    }

    @DisplayName("답변자가 다른경우 예외가 발생한다")
    @Test
    void validateAnswerOwner() {
        // given
        Question question = createQuestion("question", "questionContents");
        Answer answer = createAnswer(UserTest.JAVAJIGI, question);

        // when && then
        assertDoesNotThrow(() -> answer.validateAnswerOwner(UserTest.JAVAJIGI));
        assertThatThrownBy(() -> answer.validateAnswerOwner(UserTest.SANJIGI))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessage(CANNOT_DELETE_ANSWER_DIFFERENT_USER_MESSAGE.getMessage());
    }

    private Question createQuestion(String title, String contents) {
        return new Question(title, contents);
    }

    private Answer createAnswer(User writer, Question question) {
        return new Answer(writer, question, "Contents");
    }
}
