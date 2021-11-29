package qna.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteHistoryTest {
    @Test
    void ofQuestion() {
        final DeleteHistory deleteHistory = DeleteHistory.ofQuestion(QuestionTest.Q1);

        assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.QUESTION);
        assertThat(deleteHistory.getContentId()).isEqualTo(QuestionTest.Q1.getId());
        assertThat(deleteHistory.getDeletedBy()).isEqualTo(QuestionTest.Q1.getWriter());
    }

    @Test
    void ofAnswer() {
        final DeleteHistory deleteHistory = DeleteHistory.ofAnswer(AnswerTest.A1);

        assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER);
        assertThat(deleteHistory.getContentId()).isEqualTo(AnswerTest.A1.getId());
        assertThat(deleteHistory.getDeletedBy()).isEqualTo(AnswerTest.A1.getWriter());
    }

    @Test
    void of() {
        final Question question = new Question("title1", "contents1")
                .writeBy(UserTest.JAVAJIGI);;
        question.addAnswer(AnswerTest.A1);

        final List<DeleteHistory> deleteHistories = DeleteHistory.of(question);

        assertThat(deleteHistories).hasSize(2);
        assertThat(deleteHistories).contains(DeleteHistory.ofQuestion(question));
        assertThat(deleteHistories).contains(DeleteHistory.ofAnswer(AnswerTest.A1));
    }
}
