package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("DeleteHistoryRepository 클래스")
@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @DisplayName("저장")
    @Test
    void save() {
        final DeleteHistory saved = deleteHistoryRepository.save(DeleteHistoryTest.DH1);
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getContentId()).isNotNull(),
                () -> assertThat(saved.getDeletedById()).isNotNull()
        );
    }

    @DisplayName("DeleteHistory Id 조회")
    @Test
    void findByIdAndDeletedFalse() {
        final DeleteHistory saved = deleteHistoryRepository.save(DeleteHistoryTest.DH1);
        final DeleteHistory finded = deleteHistoryRepository.findById(saved.getId()).get();
        assertAll(
                () -> assertThat(finded).isNotNull(),
                () -> assertThat(saved.equals(finded)).isTrue()
        );
    }
}
