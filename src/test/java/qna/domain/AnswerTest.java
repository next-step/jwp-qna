package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

public class AnswerTest {

    @Test
    void 답변의_작성자가_없으면_예외를_발생시킨다() {
        //given
        User writer = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);

        //when
        assertThatThrownBy(() -> new Answer(null, question, "Writer가 비어있음"))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 질문이_비어있는_답변은_예외를_발생시킨다() {
        //given
        User writer = TestUserFactory.create("javajigi");

        //when
        assertThatThrownBy(() -> new Answer(writer, null, "Question이 비어있음"))
                .isInstanceOf(NotFoundException.class);
    }
}
