package qna.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void delete_성공() {
        List<DeleteHistory> deleteHistories = Q1.delete(UserTest.JAVAJIGI);

        Assertions.assertAll(
                () -> assertThat(Q1.isDeleted()).isTrue(),
                () -> assertThat(deleteHistories).containsExactly(new DeleteHistory(ContentType.QUESTION, Q1.getId(), Q1.getWriter()))
        );
    }

    @Test
    void delete_다른사람이_쓴_질문() {
        assertThatThrownBy(() -> Q1.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void delete_이미_삭제된_질문() {
        Question question = new Question(3L, "title", "contenst").writeBy(UserTest.JAVAJIGI);
        question.delete(UserTest.JAVAJIGI);

        assertThatThrownBy(() -> question.delete(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void addAnswer_답변이_null_일때() {
        assertThatThrownBy(() -> Q1.addAnswer(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void addAnswer_다른_질문의_답변_추가() {
        Question Q3 = new Question(3L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
        Answer answer = new Answer(Q3.getWriter(), Q3, "contents");

        assertThatThrownBy(() -> Q1.addAnswer(answer))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void addAnswer_동일한_답변_추가() {
        Question Q3 = new Question(3L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
        Answer answer = new Answer(Q3.getWriter(), Q3, "contents");

        Q3.addAnswer(answer);
        
        assertThatThrownBy(() -> Q3.addAnswer(answer))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
