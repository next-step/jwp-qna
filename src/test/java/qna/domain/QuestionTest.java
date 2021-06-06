package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;

public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void 삭제시_삭제상태가_변경되는지_확인() {
        User loginUser = UserTest.JAVAJIGI;
        Q1.delete(loginUser);
        assertThat(Q1.isDeleted()).isTrue();
    }

    @Test
    void 질문자와_로그인사용자가_동일한지_확인() {
        User answerer = UserTest.JAVAJIGI;
        assertThat(Q1.isOwner(answerer)).isTrue();
    }

    @Test
    void 질문삭제시_답변자중에서_질문자와_다른인물이_있을경우_예외발생() {
        Question question = new Question("질문입니다.", "질문내용입니다.").writeBy(UserTest.JAVAJIGI);
        question.addAnswer(AnswerTest.A2);
        assertThatThrownBy(() -> question.delete(UserTest.JAVAJIGI))
            .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 질문을_삭제하면_답변도_같이_삭제되는지_확인_SOFT삭제처리() {
        Question question = new Question("질문입니다.", "질문내용입니다.").writeBy(UserTest.JAVAJIGI);
        question.addAnswer(AnswerTest.A1);
        question.delete(UserTest.JAVAJIGI);

        assertThat(question.getAnswers().undeletedAnswers()).hasSize(0);
    }

    @Test
    void 삭제시_삭제이력을_반환하는지_확인() {
        Question question = new Question("질문입니다.", "질문내용입니다.").writeBy(UserTest.JAVAJIGI);
        question.addAnswer(AnswerTest.A1);
        DeleteHistories deleteHistories = question.delete(UserTest.JAVAJIGI);

        assertThat(deleteHistories.getDeleteHistories()).hasSize(2);
    }
}
