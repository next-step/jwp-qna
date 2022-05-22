package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeleteHistoryRepositoryTest {
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Test
    @DisplayName("DeleteHistory 저장")
    void save() {
        // when
        final DeleteHistory deleteHistory = deleteHistoryRepository.save(
                new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now()));

        // then
        assertThat(deleteHistory).isNotNull();
    }

    @Test
    @DisplayName("DeleteHistory 아이디로 찾기")
    void findById() {
        // given
        final DeleteHistory expected = deleteHistoryRepository.save(
                new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now()));

        // when
        final DeleteHistory actual = deleteHistoryRepository.findById(expected.getId()).get();
        deleteHistoryRepository.flush();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("DeleteHistory 삭제")
    void delete() {
        // given
        final DeleteHistory deleteHistory = deleteHistoryRepository.save(
                new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now()));

        // when
        deleteHistoryRepository.delete(deleteHistory);
        deleteHistoryRepository.flush();

        // then
        assertThat(deleteHistoryRepository.findById(1L)).isEmpty();
    }
}
