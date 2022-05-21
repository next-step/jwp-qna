package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @DisplayName("Question 도메인 생성 후 작성자(writer)가 null 이면 예외 처리")
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
}
