package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("삭제 내역 Repository")
class DeleteHistoryRepositoryTest extends RepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @DisplayName("저장_성공")
    @Test
    void save() {

        DeleteHistory deleteHistory = deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, answer1.getId(), javajigi, LocalDateTime.now()));

        assertAll(
                () -> assertThat(deleteHistory.getId()).isNotNull(),
                () -> assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER),
                () -> assertThat(deleteHistory.getContentId()).isNotNull(),
                () -> assertThat(deleteHistory.getCreateDate()).isNotNull(),
                () -> assertThat(deleteHistory.getDeletedById()).isNotNull());
    }

    @DisplayName("조회_성공")
    @Test
    void find() {

        DeleteHistory findDeleteHistory = deleteHistoryRepository.findById(deleteHistory.getId()).orElse(null);

        assertAll(
                () -> assertThat(findDeleteHistory.getId()).isEqualTo(deleteHistory.getId()),
                () -> assertThat(findDeleteHistory.getContentType()).isEqualTo(ContentType.QUESTION),
                () -> assertThat(findDeleteHistory.getContentId()).isNotNull(),
                () -> assertThat(findDeleteHistory.getCreateDate()).isNotNull(),
                () -> assertThat(findDeleteHistory.getDeletedById()).isNotNull());
    }
}
