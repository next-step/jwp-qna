package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.DeleteHistoryTest.D1;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Test
    @DisplayName("삭제 이력이 정상적으로 저장되어야 한다")
    void save_success() {
        // when
        DeleteHistory result = deleteHistoryRepository.save(D1);

        // then
        Optional<DeleteHistory> expected = deleteHistoryRepository.findById(result.getId());
        assertThat(expected).contains(D1);
    }
}
