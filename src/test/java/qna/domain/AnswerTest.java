package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Answer 클래스의 기능 테스트
 */
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("답변 작성 사용자 검증")
    void is_owner() {
        // then
        assertAll(
                () -> assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(A1.isOwner(UserTest.HAGI)).isFalse(),
                () -> assertThat(A1.isOwner(UserTest.SANJIGI)).isFalse()
        );
    }

    @Test
    @DisplayName("대답 등록 후 질문에 맞는 대답인지 확인")
    void add_answer() {
        // given
        QuestionTest.Q1.setId(1L);
        A1.toQuestion(QuestionTest.Q1);
        A2.toQuestion(QuestionTest.Q1);

        // then
        assertAll(
                () -> assertThat(A1.getQuestionId()).isEqualTo(QuestionTest.Q1.getId()),
                () -> assertThat(A2.getQuestionId()).isEqualTo(QuestionTest.Q1.getId()),
                () -> assertThat(A2.getQuestionId()).isNotEqualTo(QuestionTest.Q2.getId())
        );
    }
}
