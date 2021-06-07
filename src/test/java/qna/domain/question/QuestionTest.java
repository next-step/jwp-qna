package qna.domain.question;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.domain.UserTest;
import qna.domain.exception.question.AnswerOwnerNotMatchedException;
import qna.domain.exception.question.QuestionOwnerNotMatchedException;

public class QuestionTest {
    public static final Question Q1 = new Question(UserTest.JAVAJIGI, "title1", "contents1");
    public static final Question Q2 = new Question(UserTest.SANJIGI, "title2", "contents2");

    @Test
    @DisplayName("질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.")
    void when_deleteQuestion_then_deletedTrue() throws QuestionOwnerNotMatchedException, AnswerOwnerNotMatchedException {
        Question question = new Question(UserTest.SANJIGI, "title", "content");
        assertThat(question).hasFieldOrPropertyWithValue("deleted", false);
        question.deleteBy(UserTest.SANJIGI);
        assertThat(question).hasFieldOrPropertyWithValue("deleted", true);
    }
}
