package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("삭제가 정상적으로 동작했는지 확인")
    @Test
    void delete() {
        DeleteHistories deleteHistories = Q1.delete(UserTest.JAVAJIGI);

        assertAll(
            () -> assertThat(Q1.isDeleted()).isTrue(),
            () -> assertThat(deleteHistories.getDeleteHistories()).containsExactly(
                new DeleteHistory(ContentType.QUESTION, Q1.getId(), Q1.getWriter()))
        );
    }

    @DisplayName("중복된 답변이 존재하는 경우 에러가 발생되는지 확인")
    @Test
    void validateDuplicate() {
        assertThatThrownBy(() -> Q1.delete(UserTest.SANJIGI))
            .isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("두 유저 값이 동일한지 확인")
    @Test
    void isOwner() {
        assertAll(
            () -> assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue(),
            () -> assertThat(Q1.isOwner(UserTest.SANJIGI)).isFalse()
        );
    }
}
