package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired DeleteHistoryRepository deleteHistoryRepository;

    @Test
    @DisplayName("저장이 잘 되는지 테스트")
    void save() {
        DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, 7L, 8L, LocalDateTime.now());
        DeleteHistory actual = deleteHistoryRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContentType()).isEqualTo(expected.getContentType())
        );
    }

    @Test
    @DisplayName("개체를 저장한 후 다시 가져왔을 때 기존의 개체와 동일한지 테스트")
    void findById() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 7L, 8L, LocalDateTime.now());
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);

        DeleteHistory foundDeleteHistory = deleteHistoryRepository.findById(savedDeleteHistory.getId()).get();
        assertThat(foundDeleteHistory).isEqualTo(deleteHistory);
    }
}
