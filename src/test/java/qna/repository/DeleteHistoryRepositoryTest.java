package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.DeleteHistoryRepository;
import qna.domain.DeleteHistoryTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @DisplayName("save 테스트")
    @Test
    void saveTest() {
        DeleteHistory actual = deleteHistoryRepository.save(DeleteHistoryTest.questionDeleted);

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertEquals(ContentType.QUESTION, actual.getContentType()),
            () -> assertEquals(1L, actual.getContentId()),
            () -> assertEquals(1L, actual.getDeletedById()),
            () -> assertThat(actual.getCreateDate()).isNotNull()
        );
    }
}