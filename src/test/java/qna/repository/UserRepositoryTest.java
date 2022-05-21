package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;
import qna.domain.UserRepository;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private User user;
    private User user2;

    @BeforeEach
    void setUp() {
        user = JAVAJIGI;
        user2 = SANJIGI;

        user = userRepository.save(user);
        user2 = userRepository.save(user2);
    }

    @DisplayName("엔티티를 저장하면 정상적으로 저장되어야 한다")
    @Test
    void save_test() {
        assertAll(
            () -> assertThat(user).isNotNull(),
            () -> assertThat(user2).isNotNull()
        );
    }

    @DisplayName("entity 를 모두 조회하면 모두 조회되어야 한다")
    @Test
    void find_test() {
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(2);
    }

    @DisplayName("user entity 를 userId 로 조회하면 정상적으로 조회되어야 한다")
    @Test
    void find_test2() {
        Optional<User> result = userRepository.findByUserId(user.getUserId());
        Optional<User> result2 = userRepository.findByUserId(user2.getUserId());

        assertAll(
            () -> assertThat(user).isEqualTo(result.get()),
            () -> assertThat(user2).isEqualTo(result2.get())
        );
    }

    @DisplayName("entity 를 수정하면 변경된 값으로 조회되어야 한다")
    @Test
    void update_test() {
        user.update(JAVAJIGI, SANJIGI);
        userRepository.save(user);

        Optional<User> result = userRepository.findByUserId(user.getUserId());

        assertTrue(result.isPresent());

        assertAll(
            () -> assertThat(result.get().getName()).isEqualTo(SANJIGI.getName()),
            () -> assertThat(result.get().getEmail()).isEqualTo(SANJIGI.getEmail())
        );
    }

    @DisplayName("entity 를 삭제하면 정상적으로 삭제되어야 한다")
    @Test
    void deleted_test() {
        List<User> users = userRepository.findAll();

        userRepository.delete(user);
        List<User> deleted_users = userRepository.findAll();

        assertAll(
            () -> assertThat(users).hasSize(2),
            () -> assertThat(deleted_users).hasSize(1)
        );
    }
}
