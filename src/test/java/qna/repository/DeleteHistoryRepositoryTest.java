package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.DeleteHistoryTest.DELETE_HISTORY_ANSWER;
import static qna.domain.DeleteHistoryTest.DELETE_HISTORY_QUESTION;

@DisplayName("삭제 내역 Repository")
@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    private DeleteHistory deleteHistoryQuestion;

    @BeforeEach
    void setUp() {
        deleteHistoryQuestion = deleteHistoryRepository.save(DELETE_HISTORY_QUESTION);
    }

    @DisplayName("저장_성공")
    @Test
    void save() {

        DeleteHistory deleteHistory = deleteHistoryRepository.save(DELETE_HISTORY_ANSWER);

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

        DeleteHistory deleteHistory = deleteHistoryRepository.findById(deleteHistoryQuestion.getId()).orElse(null);

        assertAll(
                () -> assertThat(deleteHistory.getId()).isEqualTo(deleteHistory.getId()),
                () -> assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.QUESTION),
                () -> assertThat(deleteHistory.getContentId()).isNotNull(),
                () -> assertThat(deleteHistory.getCreateDate()).isNotNull(),
                () -> assertThat(deleteHistory.getDeletedById()).isNotNull());
    }
}
