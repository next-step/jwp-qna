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

    private DeleteHistory deleteHistory;
    private User user;

    @BeforeEach
    void setUp() {
        entityManagerHelper = new EntityManagerHelper(entityManager);

        user = userRepository.save(new User("USER", "PASSWORD", "NAME", "EMAIL"));
        deleteHistory = deleteHistoryRepository.save(
                new DeleteHistory(ContentType.ANSWER, 1L, user, LocalDateTime.now())
        );
    }

    @Test
    @DisplayName("저장을 하고, 다시 가져왔을 때 원본 객체와 같아야 한다")
    void 저장을_하고_다시_가져왔을_때_원본_객체와_같아야_한다() {
        assertThat(deleteHistory)
                .isEqualTo(deleteHistoryRepository.findById(deleteHistory.getId()).orElseThrow(EntityNotFoundException::new));
    }

    @Test
    @DisplayName("User와 연관관계를 맺었을 때, 다시 가져올 때도 맺혀있어야 한다")
    void User와_연관관계를_맺었을_때_다시_가져올_때도_맺혀있어야_한다() {
        entityManagerHelper.flushAndClear();

        DeleteHistory foundDeleteHistory = deleteHistoryRepository.findById(deleteHistory.getId())
                .orElseThrow(EntityNotFoundException::new);

        assertThat(foundDeleteHistory.isDeleteBy(userRepository.findById(user.getId()).orElseThrow(EntityNotFoundException::new)))
                .isTrue();
    }
}
