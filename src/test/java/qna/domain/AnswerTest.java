package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void 답변의_작성자가_없으면_예외를_발생시킨다() {
        //when
        assertThatThrownBy(() -> new Answer(null, QuestionTest.Q1, "Writer가 비어있음"))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 질문이_비어있는_답변은_예외를_발생시킨다() {
        //when
        assertThatThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "Question이 비어있음"))
                .isInstanceOf(NotFoundException.class);
    }
}
