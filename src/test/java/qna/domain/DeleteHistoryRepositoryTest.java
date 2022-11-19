package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
    }

    @Test
    void 삭제_이력_저장() {
        DeleteHistory history = new DeleteHistory(ContentType.ANSWER, 1L, user, LocalDateTime.now());
        DeleteHistory actual = deleteHistoryRepository.save(history);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContentType()).isEqualTo(ContentType.ANSWER),
                () -> assertThat(actual.getContentId()).isEqualTo(1L),
                () -> assertThat(actual.getDeletedByUser()).isEqualTo(user),
                () -> assertThat(actual.getCreateDate()).isNotNull()
        );
    }

    @Test
    void 삭제_이력_조회() {
        DeleteHistory history = new DeleteHistory(ContentType.ANSWER, 1L, user, LocalDateTime.now());
        deleteHistoryRepository.save(history);
        DeleteHistory actual = deleteHistoryRepository.findById(history.getId()).get();
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContentType()).isEqualTo(ContentType.ANSWER),
                () -> assertThat(actual.getContentId()).isEqualTo(1L),
                () -> assertThat(actual.getDeletedByUser()).isEqualTo(user),
                () -> assertThat(actual.getCreateDate()).isNotNull()
        );
    }
}
