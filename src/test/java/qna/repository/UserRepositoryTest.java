package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @DisplayName("유저를 저장 후 확인")
    @Test
    void save() {
        User user = userRepository.save(UserTest.JAVAJIGI);

        Optional<User> result = userRepository.findById(user.getId());

        assertThat(result).get().isEqualTo(user);
    }

    @DisplayName("유저를 저장 후 조회 확인")
    @Test
    void findAll() {
        User user1 = userRepository.save(UserTest.JAVAJIGI);
        User user2 = userRepository.save(UserTest.SANJIGI);

        List<User> result = userRepository.findAll();

        assertAll(
            () -> assertThat(result).hasSize(2),
            () -> assertThat(result).contains(user1, user2)
        );
    }

    @DisplayName("유저를 저장 후 수정 확인")
    @Test
    void update() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        user.setName(UserTest.SANJIGI.getName());

        Optional<User> result = userRepository.findById(user.getId());

        assertAll(
            () -> assertThat(result).isPresent(),
            () -> assertThat(result.get().getName()).isEqualTo(user.getName())
        );
    }

    @DisplayName("유저를 저장 후 삭제 확인")
    @Test
    void deleteUser() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        userRepository.delete(user);

        Optional<User> result = userRepository.findById(user.getId());

        assertThat(result).isNotPresent();
    }

    @DisplayName("유저 식별자로 유저 조회")
    @Test
    void findByUserId() {
        User user = userRepository.save(UserTest.JAVAJIGI);

        Optional<User> optionalUser = userRepository.findByUserId(user.getUserId());

        assertAll(
            () -> assertThat(optionalUser.isPresent()).isTrue(),
            () -> assertThat(optionalUser.get()).isEqualTo(user)
        );
    }
}