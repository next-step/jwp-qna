package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.DeleteHistoryRepository;
import qna.domain.User;
import qna.domain.UserRepository;


@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setup() {
        user = userRepository.save(new User("iamsojung", "password", "sojung", "email@gmail.com"));
    }

    @Test
    @DisplayName("DeleteHistory save 테스트")
    void saveTest() {
        DeleteHistory history = new DeleteHistory(ContentType.ANSWER, 1L, user,
            LocalDateTime.now());
        DeleteHistory actual = deleteHistoryRepository.save(history);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getContentType()).isEqualTo(ContentType.ANSWER),
            () -> assertThat(actual.getContentId()).isEqualTo(1L),
            () -> assertThat(actual.getDeleteByUser()).isEqualTo(user),
            () -> assertThat(actual.getCreateDate()).isNotNull()
        );
    }

    @Test
    @DisplayName("DeleteHistory 조회 테스트")
    void findTest() {
        DeleteHistory history = new DeleteHistory(ContentType.ANSWER, 1L, user,
            LocalDateTime.now());
        deleteHistoryRepository.save(history);
        DeleteHistory actual = deleteHistoryRepository.findById(history.getId()).get();
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getContentType()).isEqualTo(ContentType.ANSWER),
            () -> assertThat(actual.getContentId()).isEqualTo(1L),
            () -> assertThat(actual.getDeleteByUser()).isEqualTo(user),
            () -> assertThat(actual.getCreateDate()).isNotNull()
        );
    }
}