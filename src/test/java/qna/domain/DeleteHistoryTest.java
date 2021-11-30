package qna.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteHistoryTest {

    @Test
    void of() {
        final Question question = new Question("title1", "contents1")
                .writeBy(UserTest.JAVAJIGI);;
        question.addAnswer(AnswerTest.A1);

        final List<DeleteHistory> deleteHistories = DeleteHistory.of(question);

        assertThat(deleteHistories).hasSize(2);
        assertThat(deleteHistories).contains(new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter()));
        assertThat(deleteHistories).contains(new DeleteHistory(ContentType.ANSWER, AnswerTest.A1.getId(), AnswerTest.A1.getWriter()));
    }
}
