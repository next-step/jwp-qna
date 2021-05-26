package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User javajigi = UserTest.JAVAJIGI;

    private User saved;

    @BeforeEach
    void setUp() {
        saved = userRepository.save(javajigi);
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
        entityManager.createNativeQuery("ALTER TABLE user ALTER COLUMN `id` RESTART WITH 1")
                .executeUpdate();
    }

    @Test
    @DisplayName("User 저장 테스트")
    void save() {
        assertThat(saved).isEqualTo(javajigi);
    }

    @Test
    @DisplayName("User 수정 테스트")
    void update() {
        saved.setName("crong");

        Optional<User> user = userRepository.findById(saved.getId());

        assertThat(user.get().getName()).isEqualTo("crong");
    }

    @Test
    @DisplayName("User 제거 테스트")
    void delete() {
        userRepository.delete(saved);

        List<User> users = userRepository.findAll();

        assertThat(users).isEmpty();
    }

    @Test
    @DisplayName("userId로 user 조회 테스트")
    void findByUserId() {
        Optional<User> finded = userRepository.findByUserId("javajigi");

        assertThat(finded.get()).isEqualTo(javajigi);
    }
}
