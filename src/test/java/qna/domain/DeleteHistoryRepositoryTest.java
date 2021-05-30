package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private User user;
    private DeleteHistory deleteHistory;
    private DeleteHistory saved;

    @BeforeEach
    void setUp() {
        user = new User("userId", "password", "name", "email");
        User savedUser = userRepository.save(user);

        deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, savedUser, LocalDateTime.now());
        saved = deleteHistoryRepository.save(deleteHistory);
    }

    @Test
    @DisplayName("User 매핑된 참조 부분 지연로딩인지 테스트(즉시로딩 안되는지 테스트)")
    void testForUserLazy() {
        entityManager.clear();
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        Optional<DeleteHistory> maybeDeleteHistory = deleteHistoryRepository.findById(saved.getId());


        boolean isLoaded = persistenceUnitUtil.isLoaded(maybeDeleteHistory.get(), "deleteUser");
        assertThat(isLoaded).isFalse(); //지연로딩이라서 여기선 false

        String deleteUserName = maybeDeleteHistory.get().getDeleteUser().getName();
        assertAll(
                () -> assertThat(deleteUserName).isEqualTo(user.getName()),
                () -> assertThat(persistenceUnitUtil.isLoaded(maybeDeleteHistory.get(), "deleteUser")).isTrue()
        );
    }

    @Test
    @DisplayName("DeleteHistory 저장 테스트")
    void save() {
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getContentId()).isEqualTo(deleteHistory.getContentId()),
                () -> assertThat(saved.getDeleteUser()).isEqualTo(deleteHistory.getDeleteUser()),
                () -> assertThat(saved.getContentType()).isEqualTo(deleteHistory.getContentType()),
                () -> assertThat(saved.getCreateDate()).isEqualTo(deleteHistory.getCreateDate())
        );
    }

    @Test
    @DisplayName("DeleteHistory 제거 테스트")
    void delete() {
        deleteHistoryRepository.delete(saved);

        List<DeleteHistory> deleteHistories = deleteHistoryRepository.findAll();

        assertThat(deleteHistories.contains(saved)).isFalse();
    }

    @Test
    @DisplayName("DeleteHistory 조회 테스트")
    void find() {
        Optional<DeleteHistory> finded = deleteHistoryRepository.findById(saved.getId());

        assertAll(
                () -> assertThat(finded.isPresent()).isTrue(),
                () -> assertThat(finded.get()).isEqualTo(saved)
        );
    }
}
