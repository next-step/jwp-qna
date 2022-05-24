package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeleteHistoryTest {

    @Test
    @DisplayName("질문의 삭제 히스토리가 생성된다")
    void createQuestionHistory() {
        Question question = QuestionTest.Q1;
        final DeleteHistory deleteHistory = DeleteHistory.questionDeleteHistory(question);

        assertAll(() -> {
            assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.QUESTION);
            assertThat(deleteHistory.getDeletedUser()).isEqualTo(question.getWriter());
        });
    }

    @Test
    @DisplayName("응답의 삭제 히스토리가 생성된다")
    void createAnswerHistory() {
        Answer answer = AnswerTest.A1;
        final DeleteHistory deleteHistory = DeleteHistory.answersDeleteHistory(answer);

        assertAll(() -> {
            assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER);
            assertThat(deleteHistory.getDeletedUser()).isEqualTo(answer.getWriter());
        });
    }
}
