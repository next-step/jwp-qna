package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("Question 도메인 생성")
    @Test
    void test_new() {
        //given & when
        Question question = new Question("title", "contents").writeBy(UserTest.JAVAJIGI);
        //then
        assertThat(question).isNotNull();
    }

    @DisplayName("Question 의 작성자(writer)가 없으면 오류")
    @Test
    void test_null_user() {
        //given & when & then
        assertThatThrownBy(() -> new Question("title", "contents").writeBy(null))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("소유자 확인")
    @Test
    void test_is_owner() {
        //given & when & then
        assertAll(
                () -> assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(Q2.isOwner(UserTest.SANJIGI)).isTrue()
        );
    }

    @DisplayName("Question 삭제 시 deleted 상태 업데이트")
    @Test
    void test_delete() throws CannotDeleteException {
        //given
        Question question = new Question("title", "contents").writeBy(UserTest.JAVAJIGI);
        //when
        question.delete(UserTest.JAVAJIGI);
        //then
        assertThat(question.isDeleted()).isTrue();
    }

    @DisplayName("로그인 사용자가 Question 의 작성자(writer)와 동일하지 않으면 오류")
    @Test
    void test_writer_not_equals_login_user() {
        //given
        Question question = new Question("title", "contents").writeBy(UserTest.JAVAJIGI);
        //when & then
        assertThatThrownBy(() -> question.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}
