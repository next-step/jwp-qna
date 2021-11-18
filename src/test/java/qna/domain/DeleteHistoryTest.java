package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.hibernate.sql.Delete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.service.DeleteHistoryService;

@DataJpaTest
public class DeleteHistoryTest {
    public static final DeleteHistory D1
        = new DeleteHistory(ContentType.QUESTION, 1L, UserTest.JAVAJIGI, LocalDateTime.now());
    public static final DeleteHistory D2
        = new DeleteHistory(ContentType.ANSWER, 2L, UserTest.SANJIGI, LocalDateTime.now());
    @Autowired
    DeleteHistoryRepository deleteHistorys;

    @Test
    void init() {
        assertAll(
            () -> assertThat(D1.getContentType()).isEqualTo(ContentType.QUESTION),
            () -> assertThat(D1.getContentId()).isEqualTo(1L),
            () -> assertThat(D1.getDeleter()).isEqualTo(UserTest.JAVAJIGI)
        );
    }
}
