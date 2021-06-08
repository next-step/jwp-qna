package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.exceptions.CannotDeleteException;

public class QuestionRemoverTest {

    private static final User alice = new User(1L, "alice", "password", "Alice", "alice@mail");
    private static final User trudy = new User(2L, "trudy", "123456", "Trudy", "trudy@mail");

    @DisplayName("답변 0 질문 삭제")
    @Test
    void delete_EmtpyAnswers_success() {
        Question question = new Question(1L, "title", "contents").writeBy(alice);

        DeleteHistories deleteHistories = QuestionRemover.delete(question, alice);

        assertThat(deleteHistories.size()).isEqualTo(1);
        assertThat(deleteHistories.hasDeleteHistory(new DeleteHistory(question, alice))).isTrue();
    }

    @DisplayName("아직 삭제되지 않은 모든 질문 삭제 후 기록 반환")
    @Test
    void delete_UndeletedAnswers_success() {
        Question question = new Question("title", "contents").writeBy(alice);
        Answers answers = new Answers();
        Answer aliceAnswer1 = new Answer(1L, alice, question, "Alice Answer 1");
        Answer aliceAnswer2 = new Answer(2L, alice, question, "Alice Answer 2");
        Answer othersDeletedAnswer = new Answer(3L, trudy, question, "Trudy Deleted Answer");
        Answer aliceDeletedAnswer = new Answer(4L, alice, question, "Alice Deleted Answer");
        othersDeletedAnswer.delete(trudy);
        aliceDeletedAnswer.delete(alice);
        answers.add(aliceAnswer1);
        answers.add(aliceAnswer2);
        answers.add(othersDeletedAnswer);
        answers.add(aliceDeletedAnswer);

        DeleteHistories deleteHistories = QuestionRemover.delete(question, alice);

        assertThat(deleteHistories.size()).isEqualTo(3);
        assertThat(deleteHistories.hasDeleteHistory(new DeleteHistory(question, alice))).isTrue();
        assertThat(deleteHistories.hasDeleteHistory(new DeleteHistory(aliceAnswer1, alice))).isTrue();
        assertThat(deleteHistories.hasDeleteHistory(new DeleteHistory(aliceAnswer2, alice))).isTrue();
        assertThat(deleteHistories.hasDeleteHistory(new DeleteHistory(othersDeletedAnswer, trudy))).isFalse();
        assertThat(deleteHistories.hasDeleteHistory(new DeleteHistory(aliceDeletedAnswer, alice))).isFalse();
    }

    @DisplayName("다른 사람 답변이 있어 질문 삭제 실패")
    @Test
    void delete_HasOthersAnswer_ExceptionThrown() {
        Question question = new Question("title", "contents").writeBy(alice);
        Answers answers = new Answers();
        answers.add(new Answer(1L, alice, question, "Alice Answer"));
        answers.add(new Answer(2L, trudy, question, "Trudy Answer"));

        assertThatExceptionOfType(CannotDeleteException.class).isThrownBy(() ->
            QuestionRemover.delete(question, alice)
        ).withMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @DisplayName("다른 사람 질문은 삭제 실패")
    @Test
    void delete_NotOwner_ExceptionThrown() {
        Question question = new Question("title", "contents").writeBy(alice);

        assertThatExceptionOfType(CannotDeleteException.class).isThrownBy(() ->
            QuestionRemover.delete(question, trudy)
        ).withMessage("질문을 삭제할 권한이 없습니다.");
    }

}
