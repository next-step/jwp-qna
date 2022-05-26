package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class DeleteHistoryTest {

    @Test
    void 질문_삭제시_히스토리생성() {
        final DeleteHistory deleteHistory = DeleteHistory.ofQuestion(QuestionTest.Q1);
        assertAll(() -> {
            assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.QUESTION);
            assertThat(deleteHistory.getDeleteUser()).isEqualTo(QuestionTest.Q1.getWrittenBy());
        });
    }

    @Test
    void Answer_삭제시_히스토리생성() {
        final DeleteHistory deleteHistory = DeleteHistory.ofAnswer(AnswerTest.A1);
        assertAll(() -> {
            assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER);
            assertThat(deleteHistory.getDeleteUser()).isEqualTo(AnswerTest.A1.getWrittenBy());
        });
    }

}
