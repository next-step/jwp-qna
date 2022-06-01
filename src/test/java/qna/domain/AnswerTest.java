package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1,
        "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1,
        "Answers Contents2");

    @Test
    @DisplayName("답변 생성시 작성자가 null일 경우 UnAuthorizedException 예외가 발생되어야 한다.")
    void writerNullExceptionTest() {
        assertThatThrownBy(() -> new Answer(3L, null, QuestionTest.Q1, "answer contents"))
            .isExactlyInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("답변 생성시 질문이 null일 경우 NotFoundException 예외가 발생되어야 한다.")
    void questionNullExceptionTest() {
        assertThatThrownBy(() -> new Answer(3L, UserTest.JAVAJIGI, null, "answer contents3"))
            .isExactlyInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("답변의 작성자가 일치하는지 확인한다.")
    void isOwnerTest() {
        assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue();
        assertThat(A2.isOwner(UserTest.SANJIGI)).isTrue();
    }

}
