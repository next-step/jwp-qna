package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    void setUp() {
        user = new User("user", "password", "name", "email@email.com");
    }

    @Test
    @DisplayName("유저 저장")
    void save_user() {
        User actual = userRepository.save(user);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual).isEqualTo(user)
        );
    }

    @Test
    @DisplayName("유저 저장 후 조회 결과 동일성 체크")
    void find_by_id() {
        User expected = userRepository.save(user);
        Optional<User> findUser = userRepository.findById(expected.getId());
        assertTrue(findUser.isPresent());
        findUser.ifPresent(actual -> assertThat(actual).isEqualTo(expected));
    }
    
    @Test
    @DisplayName("유저 조회")
    void find_user_by_user_id() {
        User expected = userRepository.save(user);
        Optional<User> findUser = userRepository.findByUserId(expected.getUserId());
        Assertions.assertTrue(findUser.isPresent());
        findUser.ifPresent(actual ->
                assertAll(
                        () -> assertThat(actual.getId()).isNotNull(),
                        () -> assertThat(actual).isEqualTo(expected)
                )
        );
    }

    @Test
    @DisplayName("유저 변경")
    void update() {
        User loginUser = userRepository.save(user);
        User updateUser = new User("user2", "password", "name2", "email2@email.com");
        loginUser.update(loginUser, updateUser);
        assertTrue(loginUser.equalsNameAndEmail(updateUser));
    }
}
