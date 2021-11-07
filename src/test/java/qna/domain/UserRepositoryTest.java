package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@EnableJpaAuditing
@DisplayName("UserRepository 테스트")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("test_id", "Passw0rd!", "홍길동", "test@email.com");
    }

    @Test
    @DisplayName("User를 저장한다.")
    void save() {
        // when
        User savedUser = userRepository.save(user);

        // then
        assertAll(
                () -> assertThat(savedUser.getId()).isNotNull(),
                () -> assertThat(savedUser.getCreatedAt()).isNotNull(),
                () -> assertThat(savedUser.getUpdatedAt()).isNotNull(),
                () -> assertThat(savedUser).isEqualTo(user)
        );
    }

    @Test
    @DisplayName("userId로 User를 조회한다.")
    void findByUserId() {
        // given
        userRepository.save(user);

        // when
        Optional<User> userOptional = userRepository.findByUserId(user.getUserId());

        // then
        assertAll(
                () -> assertThat(userOptional.isPresent()).isTrue(),
                () -> assertThat(userOptional.get()).isEqualTo(user)
        );
    }
}
