package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.repository.entity.DeleteHistory;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    public static final DeleteHistory DELETE_HISTORY =
            new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Nested
    @DisplayName("명령")
    class Command {
        @Test
        @DisplayName("새로운 삭제 내역을 추가한다.")
        void save() {
            DeleteHistory actual = deleteHistoryRepository.save(DELETE_HISTORY);
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getContentId()).isEqualTo(DELETE_HISTORY.getContentId()),
                    () -> assertThat(actual.getContentType()).isEqualTo(DELETE_HISTORY.getContentType()),
                    () -> assertThat(actual.getCreateDate()).isEqualTo(DELETE_HISTORY.getCreateDate()),
                    () -> assertThat(actual.getDeletedById()).isEqualTo(DELETE_HISTORY.getDeletedById())
            );
        }
    }
}
