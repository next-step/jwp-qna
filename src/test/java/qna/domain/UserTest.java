package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("User 테스트")
class UserTest {
    @Autowired
    private UserRepository userRepository;

    @DisplayName("Save 확인")
    @Test
    void save_확인() {
        // given
        User user = UserTestFactory.create(1L, "user");

        // when
        User actual = userRepository.save(user);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(user.getId()),
                () -> assertThat(actual.getUserId()).isEqualTo(user.getUserId()),
                () -> assertThat(actual.getPassword()).isEqualTo(user.getPassword()),
                () -> assertThat(actual.getName()).isEqualTo(user.getName()),
                () -> assertThat(actual.getEmail()).isEqualTo(user.getEmail())
        );
    }
}
