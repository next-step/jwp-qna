package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.domain.wrapper.DeleteHistories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1", UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2", UserTest.SANJIGI);

    @Test
    void 질문데이터_소프트삭제() {
        // given
        Question question = Q1;

        // when
        question.deleteAndReturnDeleteHistories(UserTest.JAVAJIGI);

        // then
        assertThat(Q1.isDeleted()).isTrue();
    }

    @Test
    void 로그인사용자_writer_다를경우_삭제불가능() {
        assertThatThrownBy(() -> Q1.deleteAndReturnDeleteHistories(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 질문자_답변글_모든_답변자_다를시_삭제불가능() {
        // given
        Question question = new Question("title1", "contents1", UserTest.JAVAJIGI);
        question.addAnswer(new Answer(1L, UserTest.JAVAJIGI, question, "testContents"));
        question.addAnswer(new Answer(2L, UserTest.SANJIGI, question, "testContents"));

        // when & then
        assertThatThrownBy(() -> question.deleteAndReturnDeleteHistories(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 질문_답변_삭제이력_반환() {
        // given
        Question question = new Question("title1", "contents1", UserTest.JAVAJIGI);
        question.addAnswer(new Answer(1L, UserTest.JAVAJIGI, question, "testContents"));

        // when
        DeleteHistories deleteHistories = question.deleteAndReturnDeleteHistories(UserTest.JAVAJIGI);

        // then
        assertThat(deleteHistories.size()).isEqualTo(2);
    }
}
