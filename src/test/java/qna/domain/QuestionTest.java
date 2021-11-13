package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.exception.ExceptionMessage.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.exception.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private Question question;
    private List<Answer> answerList;

    @BeforeEach
    void setUp() {
        User writer = new User("writer", "123", "writer", "writer@mail.com");
        question = createQuestion("question", "contents");
        answerList = Arrays.asList(
            AnswerTest.createAnswer(writer, question),
            AnswerTest.createAnswer(writer, question));
        question.setAnswers(new Answers(answerList));
    }

    @DisplayName("로그인 사용자와 질문한 사람이 같은 경우에만 삭제가 가능하다")
    @Test
    void validateQuestionOwner() {
        assertDoesNotThrow(() -> Q1.validateQuestionOwner(UserTest.JAVAJIGI));
        assertThatThrownBy(() -> Q1.validateQuestionOwner(UserTest.SANJIGI))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessage(CANNOT_DELETE_QUESTION_MESSAGE.getMessage());
    }

    @DisplayName("질문을 삭제하면 답변들도 함께 삭제 된다")
    @Test
    void delete() {
        // when
        question.delete();

        // then
        assertTrue(question.isDeleted());
        assertThat(answerList).extracting("deleted").containsOnly(true);
    }

    @DisplayName("answer 를 포함한 question 삭제 이력을 반환한다")
    @Test
    void createDeleteHistories() {
        // when
        List<DeleteHistory> deleteHistories = question.createDeleteHistories();

        // then
        assertEquals(3, deleteHistories.size());
        assertThat(deleteHistories)
            .extracting("contentType")
            .containsExactly(ContentType.QUESTION, ContentType.ANSWER, ContentType.ANSWER);
    }

    public static Question createQuestion(String title, String contents) {
        return new Question(title, contents);
    }
}
