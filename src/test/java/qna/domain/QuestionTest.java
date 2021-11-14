package qna.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class QuestionTest {
    public static final Question Q1 = new Question(1L,"title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("로그인 사용자와 질문한 사람이 같은 경우 질문 삭제")
    @Test
    void deleteSuccess() throws CannotDeleteException {
        Question question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
        List<DeleteHistory> delete = question.delete(UserTest.JAVAJIGI);

        assertThat(question.isDeleted()).isTrue();
        assertThat(delete).hasSize(1);
    }

    @DisplayName("로그인 사용자와 질문한 사람이 같은 경우 질문,답변 삭제")
    @Test
    void test() throws CannotDeleteException {
        Question question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
        question.addAnswer(AnswerTest.A1);

        List<DeleteHistory> deleteHistories = question.delete(UserTest.JAVAJIGI);

        assertThat(question.isDeleted()).isTrue();
        assertThat(deleteHistories).hasSize(2);
    }

    @DisplayName("로그인 사용자와 질문한 사람이 다르면 예외가 발생한다.")
    @Test
    void deleteFailed() {
        Question question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);

        ThrowableAssert.ThrowingCallable throwingCallable = () -> question.delete(UserTest.SANJIGI);

        assertThatThrownBy(throwingCallable)
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("질문을 삭제할 권한이 없습니다.");
    }

}
