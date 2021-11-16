package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class DeleteHistoryTest {

    DeleteHistory ANSWER_HISTORY = DeleteHistory.OfAnswer(AnswerTest.A1);
    DeleteHistory QUESTION_HISTORY = DeleteHistory.OfQuestion(QuestionTest.Q1);

    @Test
    void contentType_ENUM_일치_검증() {
        assertAll(
            () -> assertThat(ANSWER_HISTORY.isContentType(ContentType.ANSWER)).isTrue(),
            () -> assertThat(QUESTION_HISTORY.isContentType(ContentType.QUESTION)).isTrue()
        );
    }
}
