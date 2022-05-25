package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void 질문글에_답변이_없을_때_삭제_가능_여부_확인_시_작성자이면_true가_반환되어야_한다() throws Exception {
        // given
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
}
