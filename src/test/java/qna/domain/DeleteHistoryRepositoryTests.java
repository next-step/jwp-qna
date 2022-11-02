package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("삭제 히스토리 저장소 테스트")
class DeleteHistoryRepositoryTests {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    @DisplayName("삭제 히스토리를 저장한다.")
    void save() {
        DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
        DeleteHistory deleteHistory = deleteHistoryRepository.save(expected);

        assertThat(deleteHistory).isEqualTo(expected);
    }
}
