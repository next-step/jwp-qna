package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("질문작성자와 삭제요청자가 다르면 예외발생")
    public void test_throw_exception_when_delete() {
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

        assertThatThrownBy(() -> question.delete(UserTest.SANJIGI))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("질문삭제하면 삭제필드에서 true를 반환")
    public void test_returns_true_when_delete() {
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

        question.delete(UserTest.JAVAJIGI);

        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("삭제안된 질문에서 삭제이력조회하면 예외발생")
    public void test_throw_exception_when_gethistories() {
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

        assertThatThrownBy(question::getDeleteHistories)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("삭제된 질문만 허용합니다");
    }

    @Test
    @DisplayName("삭제된질문에서 삭제이력조회")
    public void test_returns_history_when_delete() {
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

        question.delete(UserTest.JAVAJIGI);

        assertAll(
                () -> assertThat(question.getDeleteHistories()).hasSize(1),
                () -> assertThat(question.getDeleteHistories().get(0)).isEqualTo(new DeleteHistory(ContentType.QUESTION, question.getId(), UserTest.JAVAJIGI, LocalDateTime.now()))

        );

    }


}
