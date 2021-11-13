package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.fixture.UserFixture;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("User 테스트")
class UserTest {
    @Autowired
    private UserRepository userRepository;

    @DisplayName("Save 확인")
    @Test
    void save_확인() {
        // given
        User user = UserFixture.create("user");

        // when
        User actual = userRepository.save(user);

        // then
        assertThat(actual)
                .isEqualTo(user);
    }

    @DisplayName("findById 확인")
    @Test
    void findById_확인() {
        // given
        User savedUser = userRepository.save(UserFixture.create("user"));

        // when
        Optional<User> actual = userRepository.findById(savedUser.getId());

        // then
        assertThat(actual)
                .isPresent()
                .contains(savedUser);
    }

    @DisplayName("update 확인")
    @Test
    void update_확인() {
        // given
        User savedUser = userRepository.save(UserFixture.create("user"));

        // when
        savedUser.setUserId("user2");

        Optional<User> actual = userRepository.findById(savedUser.getId());

        // then
        assertThat(actual)
                .isPresent();

        assertThat(actual.get().getUserId())
                .isEqualTo("user2");
    }
}
