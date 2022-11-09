package qna.domain;

import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void writer_is_not_null_test() {
        assertThatThrownBy(() -> new Answer(null, QuestionTest.Q1, "content"))
            .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void question_is_not_null_test() {
        assertThatThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "content"))
                .isInstanceOf(NotFoundException.class);
    }

}
