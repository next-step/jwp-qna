package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;
import qna.domain.UserTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("user save() 테스트를 진행한다")
    void saveUser() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        assertAll(
                () -> assertThat(user.getId()).isNotNull(),
                () -> assertThat(user.getUserId()).isEqualTo(UserTest.JAVAJIGI.getUserId()),
                () -> assertThat(user.getEmail()).isEqualTo(UserTest.JAVAJIGI.getEmail()),
                () -> assertThat(user.getName()).isEqualTo(UserTest.JAVAJIGI.getName()),
                () -> assertThat(user.getPassword()).isEqualTo(UserTest.JAVAJIGI.getPassword())
        );
    }

    @Test
    @DisplayName("user가 삭제가 되는지 확인한다")
    void userDelete() {
        User user = userRepository.save(UserTest.SANJIGI);

        userRepository.deleteById(user.getId());

        Optional<User> result = userRepository.findById(user.getId());

        assertThat(result).isEmpty();
        assertThat(result).isNotPresent();
    }

    @Test
    @DisplayName("user를 조회한다")
    void userFind() {
        User user = userRepository.save(UserTest.SANJIGI);

        Optional<User> result = userRepository.findByUserId(user.getUserId());

        assertThat(result.get().getUserId()).isEqualTo(user.getUserId());
    }
}