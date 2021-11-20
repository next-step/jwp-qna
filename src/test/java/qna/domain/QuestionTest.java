package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);


    @Test
    @DisplayName("질문 삭제하기 로직(성공)")
    void deleteQuestion() {
        Question question1 = new Question("t1","c1").writeBy(UserTest.JAVAJIGI);
        Answer answer1 = new Answer(UserTest.JAVAJIGI, question1, "answer c1");
        Answer answer2 = new Answer(UserTest.JAVAJIGI, question1, "answer c2");
        Answer answer3 = new Answer(UserTest.JAVAJIGI, question1, "answer c3");
        assertThatCode(()->{
            List<DeleteHistory> deletedList = question1.delete(question1.getWriter());
            assertThat(deletedList).hasSize(4);
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("질문 삭제하기 로직(실패 - 답변중 다른작성자가 작성한 답변이 있는경우)")
    void deleteQuestionFail() {
        Question question1 = new Question("t1","c1").writeBy(UserTest.JAVAJIGI);
        Answer answer1 = new Answer(UserTest.JAVAJIGI, question1, "answer c1");
        Answer answer2 = new Answer(UserTest.JAVAJIGI, question1, "answer c2");
        Answer answer3 = new Answer(UserTest.SANJIGI, question1, "answer c3");
        assertThatThrownBy(()->{
            question1.delete(question1.getWriter());
        }).isInstanceOf(CannotDeleteException.class);
    }
}
