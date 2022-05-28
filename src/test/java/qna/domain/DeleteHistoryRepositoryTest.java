package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    private UserRepository userRepository;

    @DisplayName("DeleteHistory 정보 id로 조회")
    @Test
    void findById() {
        DeleteHistory deleteHistory = deleteHistoryRepository.findById(4003L).get();
        User user = userRepository.findById(1002L).get();

        assertAll(
                () -> assertThat(deleteHistory.getId()).isEqualTo(4003L),
                () -> assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER),
                () -> assertThat(deleteHistory.getContentId()).isEqualTo(3005L),
                () -> assertThat(deleteHistory.getDeletedBy()).isEqualTo(user)
        );
    }

    @DisplayName("DeleteHistory 저장 기능 확인")
    @Test
    void save() {
        User user = userRepository.findById(1001L).get();
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 3003L, user, LocalDateTime.now());
        deleteHistoryRepository.save(deleteHistory);
        deleteHistoryRepository.flush();

        DeleteHistory savedDeleteHistory = deleteHistoryRepository.findById(deleteHistory.getId()).get();
        assertThat(deleteHistory).isEqualTo(savedDeleteHistory);
    }

}
