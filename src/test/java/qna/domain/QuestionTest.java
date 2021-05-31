package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Question 클래스의 기능 테스트
 */
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("질문 작성 사용자 검증")
    void is_owner() {
        // then
        assertAll(
                () -> assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(Q1.isOwner(UserTest.HAGI)).isFalse(),
                () -> assertThat(Q1.isOwner(UserTest.SANJIGI)).isFalse()
        );
    }

    @Test
    @DisplayName("대답 등록 후 질문에 맞는 대답인지 확인")
    void add_answer() {
        // given
        Q1.setId(1L);
        Q2.setId(2L);

        // when
        Q1.addAnswer(AnswerTest.A1);

        // then
        assertAll(
                () -> assertThat(Q1).isSameAs(AnswerTest.A1.getQuestion()),
                () -> assertThat(Q2).isNotSameAs(AnswerTest.A1.getQuestion())
        );
    }
}
