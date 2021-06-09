package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.exceptions.CannotDeleteException;

public class AnswersTest {

    private static final User alice = new User(1L, "alice", "password", "Alice", "alice@mail");
    private static final User trudy = new User(2L, "trudy", "123456", "Trudy", "trudy@mail");
    private static final Question question = new Question("title", "contents").writeBy(alice);

    @DisplayName("빈 답변 목록 삭제 가능")
    @Test
    void isRemovable_EmptyAnswers_True() {
        Answers answers = new Answers();

        assertThat(answers.isRemovable(alice)).isTrue();
    }

    @DisplayName("답변 목록 전체 삭제 가능")
    @Test
    void isRemovable_OwnAnswers_True() {
        Answers answers = new Answers();
        answers.add(new Answer(alice, question, "Alice Answer 1"));
        answers.add(new Answer(alice, question, "Alice Answer 2"));
        answers.add(new Answer(alice, question, "Alice Answer 3"));

        assertThat(answers.isRemovable(alice)).isTrue();
    }

    @DisplayName("다른 사람 답변 때문에 삭제 불가능")
    @Test
    void isRemovable_HasOthersAnswer_Fail() {
        Answers answers = new Answers();
        answers.add(new Answer(alice, question, "Alice Answer 1"));
        answers.add(new Answer(alice, question, "Alice Answer 2"));
        answers.add(new Answer(trudy, question, "Trudy Answer"));

        assertThat(answers.isRemovable(alice)).isFalse();
    }

    @DisplayName("이미 지워진 다른 사람 답변 때문에 삭제 가능")
    @Test
    void isRemovable_HasOthersDeletedAnswer_True() {
        Answers answers = new Answers();
        answers.add(new Answer(alice, question, "Alice Answer 1"));
        answers.add(new Answer(alice, question, "Alice Answer 2"));
        Answer deletedAnswer = new Answer(trudy, question, "Trudy Deleted Answer");
        deletedAnswer.delete(trudy);
        answers.add(deletedAnswer);

        assertThat(answers.isRemovable(alice)).isTrue();
    }

    @DisplayName("답변 삭제 처리 후 삭제 기록 반환")
    @Test
    void delete_success() {
        Answers answers = new Answers();
        Answer aliceAnswer = new Answer(1L, alice, question, "Alice Answer");
        Answer othersDeletedAnswer = new Answer(2L, trudy, question, "Trudy Deleted Answer");
        Answer aliceDeletedAnswer = new Answer(3L, alice, question, "Alice Deleted Answer");
        othersDeletedAnswer.delete(trudy);
        aliceDeletedAnswer.delete(alice);
        answers.add(aliceAnswer);
        answers.add(othersDeletedAnswer);
        answers.add(aliceDeletedAnswer);

        DeleteHistories deleteHistories = answers.delete(alice);

        assertThat(deleteHistories.size()).isEqualTo(1);
        assertThat(deleteHistories.hasDeleteHistory(DeleteHistory.ofAnswer(aliceAnswer.getId(), alice))).isTrue();
        assertThat(aliceAnswer.isDeleted()).isTrue();
    }

    @DisplayName("다른 사람 답변 때문에 삭제 실패 후 에러 반환")
    @Test
    void delete_HasOthersAnswer_ExceptionThrown() {
        Answers answers = new Answers();
        Answer answer = new Answer(1L, trudy, question, "Trudy Answer");
        answers.add(answer);

        assertThatExceptionOfType(CannotDeleteException.class).isThrownBy(() ->
            answers.delete(alice)
        );

        assertThat(answer.isDeleted()).isFalse();
    }

}
