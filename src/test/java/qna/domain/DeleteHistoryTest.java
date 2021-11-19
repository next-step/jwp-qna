package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DeleteHistoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private EntityManager entityManager;

    private DeleteHistory history1;
    private DeleteHistory history2;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User("user1", "password", "alice",
            "alice@gmail.com");
        user2 = new User("user2", "password", "bob",
            "bob@gmail.com");

        userRepository.save(user1);
        userRepository.save(user2);

        history1 = new DeleteHistory(ContentType.QUESTION, 1L, user1
        );
        history2 = new DeleteHistory(ContentType.ANSWER, 2L, user2
        );

        deleteHistoryRepository.save(history1);
        deleteHistoryRepository.save(history2);
    }

    @AfterEach
    void tearDown() {
        deleteHistoryRepository.deleteAll();
    }

    @Test
    void test_삭제내역_저장() {
        deleteHistoryRepository.deleteAll();
        entityManager.clear();
        DeleteHistory actual = deleteHistoryRepository.save(history1);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getId()).isEqualTo(history1.getId()),
            () -> assertThat(actual.getContentType()).isEqualTo(history1.getContentType()),
            () -> assertThat(actual.getContentId()).isEqualTo(history1.getContentId()),
            () -> assertThat(actual.getDeleter()).isEqualTo(history1.getDeleter())
        );
    }

    @Test
    void test_content_id로_조회() {
        DeleteHistory actual = deleteHistoryRepository.findByContentId(history1.getContentId())
            .orElse(null);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getContentId()).isEqualTo(history1.getContentId()),
            () -> assertThat(actual).isSameAs(history1)
        );
    }

    @Test
    void test_삭제한_사용자로_조회() {
        DeleteHistory actual = deleteHistoryRepository.findByDeleter(history1.getDeleter())
            .orElse(null);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getDeleter()).isEqualTo(history1.getDeleter()),
            () -> assertThat(actual).isSameAs(history1)
        );
    }

    @Test
    void test_content_type으로_조회() {
        DeleteHistory actual = deleteHistoryRepository.findByContentType(history1.getContentType())
            .orElse(null);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getContentType()).isEqualTo(history1.getContentType()),
            () -> assertThat(actual).isSameAs(history1)
        );
    }
}