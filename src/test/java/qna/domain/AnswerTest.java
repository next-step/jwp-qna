package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @DisplayName("Answer 도메인 생성")
    @Test
    void test_new() {
        //given & when
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents");
        //then
        assertThat(answer).isNotNull();
    }

    @DisplayName("Answer 도메인 생성 시 Question 이 null 이면 NotFoundException")
    @Test
    void test_null_question() {
        //given & when & then
        assertThatThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "Answers Contents"))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("Answer 도메인 생성 시 User 가 null 이면 UnAuthorizedException")
    @Test
    void test_null_user() {
        //given & when & then
        assertThatThrownBy(() -> new Answer(null, QuestionTest.Q1, "Answers Contents"))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("소유자 확인")
    @Test
    void test_is_owner() {
        //given & when & then
        assertAll(
                () -> assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(A2.isOwner(UserTest.SANJIGI)).isTrue()
        );
    }
}
