package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(SANJIGI);

    @DisplayName("삭제 되지 않았다면 False를 리턴한다")
    @Test
    void delete_false() {
        Question question = new Question("title1", "contents1").writeBy(JAVAJIGI);
        assertThat(question.isDeleted()).isFalse();
    }

    @DisplayName("삭제 되었다면 True를 리턴한다")
    @Test
    void delete_true() {
        Question question = new Question("title1", "contents1").writeBy(JAVAJIGI);
        question.delete(JAVAJIGI);
        assertThat(question.isDeleted()).isTrue();
    }

    @DisplayName("질문자가 다르다면 예외를 던진다")
    @Test
    void delete_with_exception() {
        Question question = new Question("title1", "contents1").writeBy(JAVAJIGI);
        assertThatThrownBy(() -> question.delete(SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("삭제 되었다면 Question History를 리턴한다")
    @Test
    void delete_history() {
        Question question = new Question("title1", "contents1").writeBy(JAVAJIGI);
        List<DeleteHistory> deleteHistories = question.delete(JAVAJIGI);

        assertThat(deleteHistories).contains(DeleteHistory.of(question));
    }
}
