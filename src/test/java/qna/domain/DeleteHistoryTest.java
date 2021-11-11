package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryTest {
    DeleteHistory ANSWER_HISTORY = new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());
    DeleteHistory QUESTION_HISTORY = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());

    @Autowired
    DeleteHistoryRepository deleteHistories;

    @Test
    void save() {
        // when
        DeleteHistory actual = deleteHistories.save(ANSWER_HISTORY);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getContentId()).isEqualTo(ANSWER_HISTORY.getContentId())
        );
    }

    @Test
    void identity() {

        // when
        DeleteHistory actual = deleteHistories.save(ANSWER_HISTORY);
        DeleteHistory DB조회 = deleteHistories.findById(actual.getId()).get();

        // then
        assertThat(actual).isEqualTo(DB조회);
    }

    @Test
    void contentType_ENUM_저장확인() {

        // when
        DeleteHistory answerDeleteHistory = deleteHistories.save(ANSWER_HISTORY);
        DeleteHistory questionDeleteHistory = deleteHistories.save(QUESTION_HISTORY);

        // then
        assertAll(
            () -> assertThat(answerDeleteHistory.getContentType()).isEqualTo(ContentType.ANSWER),
            () -> assertThat(questionDeleteHistory.getContentType()).isEqualTo(ContentType.QUESTION)
        );
    }
}
