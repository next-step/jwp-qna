package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.DeleteHistoryTest.DELETE_HISTORY_1;

@DisplayName("삭제 내역 Repository")
@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @DisplayName("저장_성공")
    @Test
    void save() {

        DeleteHistory deleteHistory = deleteHistoryRepository.save(DELETE_HISTORY_1);

        assertAll(
                () -> assertThat(deleteHistory.getId()).isNotNull(),
                () -> assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER),
                () -> assertThat(deleteHistory.getContentId()).isNotNull(),
                () -> assertThat(deleteHistory.getCreateDate()).isNotNull(),
                () -> assertThat(deleteHistory.getDeletedById()).isNotNull());
    }
}
