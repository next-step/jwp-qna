package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("삭제이력 저장소 테스트")
public class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @BeforeEach
    void 테스트_수행_전_데이터_일괄삭제() {
        deleteHistoryRepository.deleteAll();
    }

    @Test
    @DisplayName("삭제이력 저장")
    void 저장() {
        DeleteHistory deleteHistory = DeleteHistoryTest.D1;
        DeleteHistory saved = deleteHistoryRepository.save(deleteHistory);

        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(deleteHistory.getDeletedById()).isEqualTo(saved.getDeletedById()),
                () -> assertThat(deleteHistory.getContentId()).isEqualTo(saved.getContentId()),
                () -> assertThat(deleteHistory.getCreateDate()).isEqualTo(saved.getCreateDate()),
                () -> assertThat(deleteHistory.getContentType()).isEqualTo(saved.getContentType())
        );
    }

    @Test
    @DisplayName("삭제이력 삭제")
    void 삭제() {
        DeleteHistory deleteHistory = deleteHistoryRepository.save(DeleteHistoryTest.D1);
        deleteHistoryRepository.delete(deleteHistory);

        assertThat(deleteHistoryRepository.findById(deleteHistory.getId())).isEmpty();
    }
}
