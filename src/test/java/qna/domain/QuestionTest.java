package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.UnAuthorizedException;
import qna.constant.ErrorCode;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

    @Test
    void 질문의_작성자가_없으면_예외를_발생시킨다() {
        //given
        Question question = new Question("title3", "Writer가 비어있음");

        //when
        assertThatThrownBy(() -> question.writeBy(null))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 질문한_작성자가_아니면_예외를_발생시킨다() {
        //given
        User writer = TestUserFactory.create("javajigi");
        User fakeWriter = TestUserFactory.create("sanjigi");
        Question question = TestQuestionFactory.create(writer);

        //when
        assertThatThrownBy(() -> question.validateSameUser(fakeWriter))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage(ErrorCode.질문_삭제_권한_없음.getErrorMessage());
    }

    @Test
    void 질문_삭제여부_true_변경() {
        //given
        User writer = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);

        //when
        question.changeDeleted(true);

        //then
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    void 질문_삭제여부_false_변경() {
        //given
        User writer = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);

        //when
        question.changeDeleted(false);

        //then
        assertThat(question.isDeleted()).isFalse();
    }

    @Test
    void 질문자와_동일한_유저이면_질문을_삭제한다() {
        //given
        User writer = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);
        question.changeDeleted(false);

        //when
        question.delete(writer);

        //then
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    void 질문자와_동일한_유저가_질문_삭제_요청_시_예외를_발생시킨다() {
        //given
        User writer = TestUserFactory.create("javajigi");
        User fakeWriter = TestUserFactory.create("sanjigi");
        Question question = TestQuestionFactory.create(writer);
        question.changeDeleted(false);

        //when
        assertThatThrownBy(() -> question.delete(fakeWriter))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage(ErrorCode.질문_삭제_권한_없음.getErrorMessage());
    }

    @Test
    void 질문_삭제_요청_시_질문자와_동일하지_않은_유저가_쓴_답변이_있다면_예외를_발생시킨다() {
        //given
        User writer = TestUserFactory.create("javajigi");
        User fakeWriter = TestUserFactory.create("sanjigi");
        Question question = TestQuestionFactory.create(writer);
        Answer answer1 = new Answer(writer, question, "정상 writer");
        Answer answer2 = new Answer(fakeWriter, question, "fake writer");
        question.changeDeleted(false);
        answer1.changeDeleted(false);
        answer2.changeDeleted(false);

        //when
        assertThatThrownBy(() -> question.delete(writer))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage(ErrorCode.답변_중_다른_사람이_쓴_답변_있어_삭제_못함.getErrorMessage());
    }

    @Test
    void 질문_삭제_요청_성공() {
        //given
        User writer = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);
        Answer answer1 = new Answer(writer, question, "정상 writer");
        Answer answer2 = new Answer(writer, question, "정상 writer");
        question.changeDeleted(false);
        answer1.changeDeleted(false);
        answer2.changeDeleted(false);

        //when
        question.delete(writer);

        //then
        assertAll(
                () -> assertThat(question.isDeleted()).isTrue(),
                () -> assertThat(answer1.isDeleted()).isTrue(),
                () -> assertThat(answer2.isDeleted()).isTrue()
        );
    }
}
