package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void Answer_객체_생성_시_Writer가_비어있으면_에러_발생() {
        assertThatThrownBy(() -> new Answer(null, QuestionTest.Q1, "Writer가 비어있음"))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void Answer_객체_생성_시_Question이_비어있으면_에러_발생() {
        assertThatThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "Question이 비어있음"))
                .isInstanceOf(NotFoundException.class);
    }
}
