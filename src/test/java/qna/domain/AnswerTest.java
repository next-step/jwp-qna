package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.NotFoundException;
import qna.UnAuthorizedException;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("생성 테스트")
    void create() {
        // given
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        // when & then
        assertAll(
            () -> assertThat(answer.getQuestion()).isEqualTo(A1.getQuestion()),
            () -> assertThat(answer.getWriter()).isEqualTo(A1.getWriter()),
            () -> assertThat(answer.getContents()).isEqualTo(A1.getContents())
        );
    }

    @Test
    @DisplayName("생성 예외 테스트")
    void create_exception() {
        // given & when & then
        Assertions.assertThrows(UnAuthorizedException.class, () -> new Answer(null, QuestionTest.Q1, "Answers Contents1"));
        Assertions.assertThrows(NotFoundException.class, () -> new Answer(UserTest.JAVAJIGI, null, "Answers Contents1"));
    }

    @Test
    @DisplayName("작성자 비교 테스트")
    void isOwner() {
        // given & when & then
        assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue();
        assertThat(A1.isOwner(UserTest.SANJIGI)).isFalse();
    }
}
