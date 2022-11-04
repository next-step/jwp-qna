package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.constant.ErrorCode;

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

    @Test
    void 답변이_등록되면_질문의_답변에도_추가된다() {
        //given
        User writer = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);
        int actual = question.getAnswers().size();

        //when
        new Answer(writer, question, "Question 리스트 추가");
        int expect = question.getAnswers().size();

        //then
        assertThat(actual+1).isEqualTo(expect);
    }

    @Test
    void toString_테스트() {
        //given
        User writer = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);
        Answer answer = new Answer(writer, question, "toString 테스트");

        //then
        System.out.println("question= "+question);
        System.out.println("answer= "+answer);
    }

    @Test
    void 답변한_작성자가_아니면_예외를_발생시킨다() {
        //given
        User writer = TestUserFactory.create("javajigi");
        User fakeWriter = TestUserFactory.create("sanjigi");
        Question question = TestQuestionFactory.create(writer);
        Answer answer = new Answer(writer, question, "답변한 작성자가 아니면 예외 발생");

        //when
        assertThatThrownBy(() -> answer.validateSameUser(fakeWriter))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage(ErrorCode.답변_중_다른_사람이_쓴_답변_있어_삭제_못함.getErrorMessage());
    }
}
