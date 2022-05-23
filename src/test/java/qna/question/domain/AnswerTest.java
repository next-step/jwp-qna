package qna.question.domain;

import org.junit.jupiter.api.Test;
import qna.question.exception.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.user.domain.UserTest.JAVAJIGI;
import static qna.user.domain.UserTest.SANJIGI;

public class AnswerTest {
    public static final Answer A1 = new Answer(null, JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(null, SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void 답변을_작성하면_작성한_사용자에게_할당되어야_한다() {
        assertThat(A1.isOwner(JAVAJIGI)).isTrue();
        assertThat(A1.isOwner(SANJIGI)).isFalse();
    }

    @Test
    void 질문에_답변을_추가하면_답변에_해당_질문과_연결되어야_한다() {
        Question question = new Question(1L, "content", "title");
        Answer answer = new Answer(null, JAVAJIGI, question, "Answers Contents1");

        question.addAnswer(answer);

        assertThat(answer.getQuestionId()).isEqualTo(question.getId());
    }

    @Test
    void 답변을_삭제하면_해당_답변은_삭제_여부가_true_이어야_한다() throws CannotDeleteException {
        Answer answer = new Answer(null, JAVAJIGI, new Question(), "Answers Contents1");

        assertThat(answer.isDeleted()).isFalse();

        answer.delete(JAVAJIGI);
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    void 답변을_삭제할_때_자신이_생성한_답변이_아닌경우_예외가_발생해야_한다() {
        Answer answer = new Answer(null, JAVAJIGI, QuestionTest.savedQuestion1, "Answers Contents1");

        assertThatThrownBy(() -> answer.delete(SANJIGI)).isInstanceOf(CannotDeleteException.class);
    }
}
