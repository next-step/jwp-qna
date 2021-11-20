package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

class AnswersTest {
    @DisplayName("답변 추가")
    @Test
    void add() {
        Answers answers = new Answers();

        answers.add(AnswerTest.A1);

        assertThat(answers).isEqualTo(new Answers(Arrays.asList(AnswerTest.A1)));
    }

    @DisplayName("모든 답변자가 작성자인지 확인")
    @Test
    void validateOwner() {
        Answers answers = new Answers(Arrays.asList(AnswerTest.A1, AnswerTest.A2));

        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> answers.validateOwner(UserTest.JAVAJIGI))
            .withMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @DisplayName("삭제")
    @Test
    void delete() {
        Answers answers = new Answers(Arrays.asList(AnswerTest.A1, AnswerTest.A1));
        List<DeleteHistory> expected = new ArrayList<>();
        expected.add(
            new DeleteHistory(ContentType.ANSWER, AnswerTest.A1.getId(), AnswerTest.A1.getWriter(), LocalDateTime
                .now()));
        expected.add(
            new DeleteHistory(ContentType.ANSWER, AnswerTest.A1.getId(), AnswerTest.A1.getWriter(), LocalDateTime
                .now()));

        List<DeleteHistory> deleteHistories = answers.delete(UserTest.JAVAJIGI);

        assertThat(deleteHistories).isEqualTo(expected);
    }
}