package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.UnAuthorizedException;

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
                .hasMessage("질문을 삭제할 권한이 없습니다.");
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
}
