package qna.question.domain;

import org.junit.jupiter.api.Test;
import qna.user.domain.UserTest;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerTest {
    public static final Answer A1 = new Answer(null, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(null, UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void 답변을_작성하면_작성한_사용자에게_할당되어야_한다() {
        assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue();
        assertThat(A1.isOwner(UserTest.SANJIGI)).isFalse();
    }

    @Test
    void 질문에_답변을_추가하면_답변에_해당_질문과_연결되어야_한다() {
        Answer answer = new Answer(null, UserTest.JAVAJIGI, QuestionTest.savedQuestion1, "Answers Contents1");

        answer.toQuestion(QuestionTest.savedQuestion1);

        assertThat(answer.getQuestionId()).isEqualTo(QuestionTest.savedQuestion1.getId());
        assertThat(answer.getQuestionId()).isNotEqualTo(QuestionTest.savedQuestion2.getId());
    }

    @Test
    void 답변을_삭제하면_해당_답변은_삭제_여부가_true_이어야_한다() {
        Answer answer = new Answer(null, UserTest.JAVAJIGI, QuestionTest.savedQuestion1, "Answers Contents1");

        assertThat(answer.isDeleted()).isFalse();

        answer.answerDelete();
        assertThat(answer.isDeleted()).isTrue();
    }
}
