package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("질문작성자와 삭제요청자가 다르면 예외발생")
    public void test_throw_exception() {
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

        assertThatThrownBy(() -> question.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("질문삭제하면 삭제필드에서 true를 반환")
    public void test_returns_true_when_question_deleted() throws CannotDeleteException {
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

        question.delete(UserTest.JAVAJIGI);

        assertThat(question.isDeleted()).isTrue();

    }

}
