package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void 답변_객체_생성_시_writer가_비어있으면_UnAuthorizedException_예외가_발생한다() {
        //when
        assertThatThrownBy(() -> new Answer(null, QuestionTest.Q1, "Writer가 비어있음"))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 답변_객체_생성_시_question이_비어있으면_NotFoundException_예외가_발생한다() {
        //when
        assertThatThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "Question이 비어있음"))
                .isInstanceOf(NotFoundException.class);
    }
}
