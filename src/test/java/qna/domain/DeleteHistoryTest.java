package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryTest {
    DeleteHistory ANSWER_HISTORY = new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, LocalDateTime.now());
    DeleteHistory QUESTION_HISTORY = new DeleteHistory(ContentType.QUESTION, 1L, UserTest.JAVAJIGI,
        LocalDateTime.now());

    @Test
    void contentType_ENUM_일치_검증() {
        assertAll(
            () -> assertThat(ANSWER_HISTORY.isContentType(ContentType.ANSWER)).isTrue(),
            () -> assertThat(QUESTION_HISTORY.isContentType(ContentType.QUESTION)).isTrue()
        );
    }
}
