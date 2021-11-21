package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;

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

    @DisplayName("삭제")
    @Test
    void delete() {
        Answers answers = new Answers(Arrays.asList(AnswerTest.A2, AnswerTest.A3, AnswerTest.A4));
        DeleteHistories expected = new DeleteHistories(Arrays.asList(
            new DeleteHistory(ContentType.ANSWER, AnswerTest.A2.getId(), AnswerTest.A2.getWriter(), LocalDateTime.now()),
            new DeleteHistory(ContentType.ANSWER, AnswerTest.A3.getId(), AnswerTest.A3.getWriter(), LocalDateTime.now()),
            new DeleteHistory(ContentType.ANSWER, AnswerTest.A3.getId(), AnswerTest.A3.getWriter(), LocalDateTime.now())
            ));

        DeleteHistories deleteHistories = answers.delete(UserTest.SANJIGI);

        assertThat(expected).isEqualTo(deleteHistories);
    }
}