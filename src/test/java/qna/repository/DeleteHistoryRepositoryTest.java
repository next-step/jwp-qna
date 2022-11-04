package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.DeleteHistory;
import qna.domain.DeleteHistoryTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @DisplayName("삭제 이력을 저장할 수 있다")
    @Test
    void save() {
        DeleteHistory actual = deleteHistoryRepository.save(DeleteHistoryTest.DH1);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull()
        );
    }
}
