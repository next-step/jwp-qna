package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.EntityManagerHelper;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private EntityManagerHelper entityManagerHelper;

    private User savedUser;

    private DeleteHistory deleteHistory;

    @BeforeEach
    void setUp() {
        entityManagerHelper = new EntityManagerHelper(entityManager);

        savedUser = userRepository.save(new User("USER", "PASSWORD", "NAME", "EMAIL@EMAIL.COM"));

        deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, savedUser, LocalDateTime.now());
    }

    @Test
    @DisplayName("저장을 하고, 다시 가져왔을 때 원본 객체와 같아야 한다")
    void 저장을_하고_다시_가져왔을_때_원본_객체와_같아야_한다() {
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);

        DeleteHistory foundDeleteHistory = deleteHistoryRepository.findById(deleteHistory.getId())
                .orElseThrow(EntityNotFoundException::new);

        assertThat(savedDeleteHistory)
                .isEqualTo(foundDeleteHistory);
    }

    @Test
    @DisplayName("User와 연관관계를 맺었을 때, 다시 가져올 때도 맺혀있어야 한다")
    void User와_연관관계를_맺었을_때_다시_가져올_때도_맺혀있어야_한다() {
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);

        entityManagerHelper.flushAndClear();

        DeleteHistory foundDeleteHistory = deleteHistoryRepository.findById(savedDeleteHistory.getId())
                .orElseThrow(EntityNotFoundException::new);

        User foundUser = userRepository.findById(savedUser.getId())
                .orElseThrow(EntityNotFoundException::new);

        assertThat(foundDeleteHistory.isDeleteBy(foundUser))
                .isTrue();
    }
}
