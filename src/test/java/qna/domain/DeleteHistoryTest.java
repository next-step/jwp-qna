package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class DeleteHistoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @DisplayName("deleteHistory 저장 테스트")
    @Test
    void deleteHistorySaveTest() {
        LocalDateTime now =  LocalDateTime.now();
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());

        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);

        assertAll(() -> {
            assertThat(savedDeleteHistory.getId(), is(notNullValue()));
            assertThat(savedDeleteHistory.getContentType(), is(ContentType.QUESTION));
            assertThat(savedDeleteHistory.getContentId(), is(deleteHistory.getContentId()));
            assertThat(savedDeleteHistory.getDeletedById(), is(deleteHistory.getDeletedById()));
            assertTrue(savedDeleteHistory.getCreateDate().isEqual(now) || savedDeleteHistory.getCreateDate().isAfter(now));
        });
    }
}
