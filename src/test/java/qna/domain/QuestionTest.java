package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("게시자 확인")
    void isOwner() {
        assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue();
    }

    @Test
    @DisplayName("게시자 확인 - 다른 게시자")
    void isNotOwner() {
        assertThat(Q1.isNotOwner(UserTest.SANJIGI)).isTrue();
    }

    @Test
    @DisplayName("삭제 - 질문 작성자와 로그인 유저가 다를 때")
    void delete_isNotOwner() {
        assertThatThrownBy(() -> Q1.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("삭제 - 질문 작성자와 로그인 유저가 같을 때")
    void delete_isOwner() {
        assertThatCode(() -> Q1.delete(UserTest.JAVAJIGI))
                .doesNotThrowAnyException();
    }
}
