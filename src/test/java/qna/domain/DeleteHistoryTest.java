package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import qna.domain.deletehistory.DeleteHistory;
import qna.domain.deletehistory.ContentType;

public class DeleteHistoryTest {

    DeleteHistory ANSWER_HISTORY = DeleteHistory.OfAnswer(AnswerTest.A1, AnswerTest.A1.getWriter());
    DeleteHistory QUESTION_HISTORY = DeleteHistory.OfQuestion(QuestionTest.Q1,
        QuestionTest.Q1.getWriter());

    @Test
    void contentType_ENUM_일치_검증() {
        assertAll(
            () -> assertThat(ANSWER_HISTORY.getContentType()).isEqualTo(ContentType.ANSWER),
            () -> assertThat(QUESTION_HISTORY.getContentType()).isEqualTo(ContentType.QUESTION)
        );
    }
}
