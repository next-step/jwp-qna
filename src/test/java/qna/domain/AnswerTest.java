package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.FixtureUtils.*;

import org.junit.jupiter.api.Test;

import qna.NotFoundException;
import qna.UnAuthorizedException;

public class AnswerTest {
    @Test
    void 객체_생성_유저_null일경우_UnAuthorizedException() {
        assertThatThrownBy(() -> ANSWER1(null, QUESTION1_WRITE_BY_JAVAJIGI())).isInstanceOf(
            UnAuthorizedException.class);
    }

    @Test
    void 객체_생성_질문_null일경우_UnAuthorizedException() {
        assertThatThrownBy(() -> ANSWER1(JAVAJIGI(), null)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 동등성() {
        assertAll(
            () -> assertThat(ANSWER1_WRITE_BY_JAVAJIGI()).isEqualTo(ANSWER1_WRITE_BY_JAVAJIGI()),
            () -> assertThat(ANSWER1_WRITE_BY_JAVAJIGI()).isNotEqualTo(ANSWER2_WRITE_BY_JAVAJIGI())
        );
    }

    @Test
    void 작성자_확인() {
        assertAll(
            () -> assertThat(ANSWER1_WRITE_BY_JAVAJIGI().isOwner(JAVAJIGI())).isTrue(),
            () -> assertThat(ANSWER1_WRITE_BY_JAVAJIGI().isOwner(SANJIGI())).isFalse()
        );
    }

    @Test
    void 질문_변경() {
        Question q1 = QUESTION1_WRITE_BY_JAVAJIGI();
        Question q2 = QUESTION2_WRITE_BY_JAVAJIGI();
        Answer a1 = ANSWER1_WRITE_BY_JAVAJIGI();
        a1.toQuestion(q2);

        assertAll(
            () -> assertThat(a1.getQuestion()).isNotEqualTo(q1),
            () -> assertThat(a1.getQuestion()).isEqualTo(q2),
            () -> assertThat(q1.getAnswers()).doesNotContain(a1),
            () -> assertThat(q2.getAnswers()).contains(a1)
        );
    }

    @Test
    void toString_순환참조_테스트() {
        assertDoesNotThrow(() -> {
            assertThat(ANSWER1_WRITE_BY_JAVAJIGI().toString()).isNotNull();
        });
    }
}
