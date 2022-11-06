package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("작성자가 null 일 때 예외발생 확인")
    void writerException() {
        Assertions.assertThatThrownBy(() -> new Answer(1L, null, QuestionTest.Q1, "contents"))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("질문이 null 일 때 예외발생 확인")
    void questionException() {
        Assertions.assertThatThrownBy(() -> new Answer(1L, UserTest.JAVAJIGI, null, "contents"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("답변 작성자 확인 테스트")
    void owner() {
        Answer answer = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        Assertions.assertThat(answer.isOwner(UserTest.JAVAJIGI)).isTrue();
    }

    @Test
    @DisplayName("삭제 여부 true 테스트")
    void delete() {
        Answer answer = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        answer.deleted();
        Assertions.assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("삭제 여부 false 테스트")
    void delete2() {
        Answer answer = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        Assertions.assertThat(answer.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("equals 테스트 (동등한 경우)")
    void equals1() {
        Answer actual = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        Answer expected = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "contents");

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("equals 테스트 (동등하지 않은 경우)")
    void equals2() {
        Answer actual = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        Answer expected = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q1, "contents");

        Assertions.assertThat(actual).isNotEqualTo(expected);
    }
}
