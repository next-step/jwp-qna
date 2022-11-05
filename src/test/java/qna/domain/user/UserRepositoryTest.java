package qna.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import qna.NotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DirtiesContext
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("사용자가 정상적으로 등록되있는지 테스트 한다")
    void saveUserTest() {
        User user = UserTest.createUser("user");
        User save = userRepository.save(user);
        assertAll(
                () -> assertThat(save.getId()).isEqualTo(user.getId()),
                () -> assertThat(save.getUserId()).isEqualTo(user.getUserId()),
                () -> assertThat(save.getName()).isEqualTo(user.getName()),
                () -> assertThat(save.getPassword()).isEqualTo(user.getPassword()),
                () -> assertThat(save.getEmail()).isEqualTo(user.getEmail()),
                () -> assertThat(save.getCreatedAt()).isEqualTo(user.getCreatedAt()),
                () -> assertThat(save.getUpdatedAt()).isEqualTo(user.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("사용자를 등록하고 조회되는지 테스트한다")
    void byIdAndDeletedFalseTest() {
        User user = userRepository.save(UserTest.createUser("user"));
        User getUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException());
        assertAll(
                () -> assertThat(getUser).isNotNull(),
                () -> assertThat(getUser.getId()).isEqualTo(user.getId()),
                () -> assertThat(getUser.getUserId()).isEqualTo(user.getUserId()),
                () -> assertThat(getUser.getName()).isEqualTo(user.getName()),
                () -> assertThat(getUser.getPassword()).isEqualTo(user.getPassword()),
                () -> assertThat(getUser.getEmail()).isEqualTo(user.getEmail()),
                () -> assertThat(getUser.getCreatedAt()).isEqualTo(user.getCreatedAt()),
                () -> assertThat(getUser.getUpdatedAt()).isEqualTo(user.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("사용자를 id로 삭제되는지 테스트한다")
    void deleteByIdTest() {
        User user = userRepository.save(UserTest.createUser("user"));
        userRepository.deleteById(user.getId());
        Optional<User> getUser = userRepository.findById(user.getId());
        assertThat(getUser.isPresent()).isFalse();
    }

}
