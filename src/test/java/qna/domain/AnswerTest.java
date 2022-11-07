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
        assertThatThrownBy(() -> A1(null, Q1())).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 객체_생성_질문_null일경우_UnAuthorizedException() {
        assertThatThrownBy(() -> A1(JAVAJIGI(), null)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 동등성() {
        assertAll(
            () -> assertThat(A1()).isEqualTo(A1()),
            () -> assertThat(A1()).isNotEqualTo(A2())
        );
    }

    @Test
    void 작성자_확인() {
        assertAll(
            () -> assertThat(A1().isOwner(JAVAJIGI())).isTrue(),
            () -> assertThat(A1().isOwner(SANJIGI())).isFalse()
        );
    }

    @Test
    void 질문_변경() {
        Question q1 = Q1();
        Question q2 = Q2();
        Answer a1 = A1();
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
            assertThat(A1().toString()).isNotNull();
        });
    }
}
