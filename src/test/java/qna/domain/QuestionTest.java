package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private Question question;
    private User writer;

    @BeforeEach
    void setUp() {
        question = new Question(1L, "title", "contents");
        writer = new User(1L, "writer", "password", "name", "email");
        question.writeBy(writer);
    }

    @Test
    void 질문글의_작성자를_설정할_수_있어야_한다() {
        // when
        question.writeBy(UserTest.JAVAJIGI);

        // then
        assertThat(question.getWriter()).isEqualTo(UserTest.JAVAJIGI);
    }

    @Test
    void 질문글의_작성자인지_여부를_확인할_수_있어야_한다() {
        // given
        final User a1Writer = UserTest.JAVAJIGI;
        final User a2Writer = UserTest.SANJIGI;

        // when and then
        assertThat(Q1.isOwner(a1Writer)).isTrue();
        assertThat(Q1.isOwner(a2Writer)).isFalse();

        assertThat(Q2.isOwner(a1Writer)).isFalse();
        assertThat(Q2.isOwner(a2Writer)).isTrue();
    }

    @Test
    void 질문글에_답변을_추가할_수_있어야_한다() {
        // given
        final Question question = new Question(1L, "title", "contents");
        final Answer newAnswer = AnswerTest.A1;

        // when
        question.addAnswer(newAnswer);

        // then
        assertThat(question).isEqualTo(AnswerTest.A1.getQuestion());

        // finally
        AnswerTest.A1.toQuestion(QuestionTest.Q1);
    }

    @Test
    void 답변이_추가되면_Question과_Answer의_연관관계가_올바르게_설정되어야_한다() {
        // given
        final Question originQuestion = new Question(1L, "origin title", "origin contents");
        final Answer answer = new Answer(writer, originQuestion, "answer");

        final Question newQuestion = new Question(2L, "new title", "new contents");

        // when
        newQuestion.addAnswer(answer);

        // then
        assertThat(answer.getQuestion()).isEqualTo(newQuestion);
    }

    @Test
    void 질문글에_답변이_없을_때_삭제_가능_여부_확인_시_작성자이면_true가_반환되어야_한다() throws Exception {
        // when and then
        assertThat(question.canBeDeletedBy(writer)).isTrue();
    }

    @Test
    void 질문글에_답변이_없을_때_삭제_가능_여부_확인_시_작성자가_아니면_CannotDeleteException이_발생해야_한다() {
        // given
        final User loginUser = new User(2L, "ttungga", "password", "name", "email");

        // when and then
        assertThatThrownBy(() -> question.canBeDeletedBy(loginUser))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    void 질문글에_답변이_있을_때_삭제_가능_여부_확인_시_모든_답변이_질문글_작성자가_작성한_것이면_true가_반환되어야_한다() throws Exception {
        // given
        new Answer(1L, writer, question, "answer1");
        new Answer(2L, writer, question, "answer2");

        // when and then
        assertThat(question.canBeDeletedBy(writer)).isTrue();
    }

    @Test
    void 질문글에_답변이_있을_때_삭제_가능_여부_확인_시_답변이_하나라도_질문글_작성자가_작성한_게_아니면_CannotDeleteException이_발생해야_한다() {
        // given
        final User differentWriter = new User(2L, "different", "password", "name", "email");
        new Answer(1L, differentWriter, question, "answer");

        // when and then
        assertThatThrownBy(() -> question.canBeDeletedBy(writer))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @Test
    void 질문글에_답변이_없을_때_질문글을_삭제하면_삭제된_상태로_변경되고_삭제이력_목록이_반환되어야_한다() {
        // when
        List<DeleteHistory> deleteHistories = question.delete(writer);

        // then
        assertThat(question.isDeleted()).isTrue();
        assertThat(deleteHistories.size()).isEqualTo(1);
    }

    @Test
    void 질문글에_답변이_있을_때_질문글을_삭제하면_질문글과_답변들이_삭제된_상태로_변경되고_삭제이력_목록이_반환되어야_한다() {
        // given
        final Answer answer1 = new Answer(1L, writer, question, "answer1");
        final Answer answer2 = new Answer(2L, writer, question, "answer2");

        // when
        List<DeleteHistory> deleteHistories = question.delete(writer);

        // then
        assertThat(question.isDeleted() && answer1.isDeleted() && answer2.isDeleted()).isTrue();
        assertThat(deleteHistories.size()).isEqualTo(3);
    }
}
