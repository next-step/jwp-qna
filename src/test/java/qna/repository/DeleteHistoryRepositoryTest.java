package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.DeleteHistory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.DeleteHistoryTest.DH1;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @DisplayName("삭제 이력을 저장할 수 있다")
    @Test
    void save() {
        DeleteHistory actual = deleteHistoryRepository.save(DH1);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContentId()).isEqualTo(DH1.getContentId()),
                () -> assertThat(actual.getDeletedBy()).isEqualTo(DH1.getDeletedBy()),
                () -> assertThat(actual.getCreateDate()).isEqualTo(DH1.getCreateDate())
        );
    }
}
