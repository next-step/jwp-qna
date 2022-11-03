package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("삭제이력 저장소 테스트")
public class DeleteHistoryRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    private User user;
    private final DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, user, LocalDateTime.now());

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User(1L, "chaeeun", "password", "name", "chaeeun@gmail.com"));
    }

    @Test
    @DisplayName("삭제이력 저장")
    void 저장() {
        DeleteHistory saved = deleteHistoryRepository.save(deleteHistory);

        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(deleteHistory.getDeletedBy()).isEqualTo(saved.getDeletedBy()),
                () -> assertThat(deleteHistory.getContentId()).isEqualTo(saved.getContentId()),
                () -> assertThat(deleteHistory.getCreateDate()).isEqualTo(saved.getCreateDate()),
                () -> assertThat(deleteHistory.getContentType()).isEqualTo(saved.getContentType())
        );
    }

    @Test
    @DisplayName("삭제이력 삭제")
    void 삭제() {
        DeleteHistory target = deleteHistoryRepository.save(deleteHistory);
        deleteHistoryRepository.delete(target);

        assertThat(deleteHistoryRepository.findById(deleteHistory.getId())).isEmpty();
    }
}
