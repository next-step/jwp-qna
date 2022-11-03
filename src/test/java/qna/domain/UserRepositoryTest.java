package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("사용자를 저장할 수 있어야 한다.")
    void save_user() {
        User user = userRepository.save(JAVAJIGI);
        assertThat(user.getId()).isNotNull();

        assertAll(
                () -> assertThat(user.getUserId()).isEqualTo(JAVAJIGI.getUserId()),
                () -> assertThat(user.getPassword()).isEqualTo(JAVAJIGI.getPassword()),
                () -> assertThat(user.getName()).isEqualTo(JAVAJIGI.getName()),
                () -> assertThat(user.getEmail()).isEqualTo(JAVAJIGI.getEmail()),
                () -> assertThat(user.getCreatedAt()).isNotNull()
        );
    }

    @Test
    @DisplayName("userId로 User를 조회할 수 있어야 한다.")
    void get_user_by_id() {
        List<User> users = userRepository.saveAll(Arrays.asList(JAVAJIGI, SANJIGI));

        User savedUser = userRepository.findByUserId(users.get(0).getUserId()).get();

        assertThat(users.get(0)).isEqualTo(savedUser);
    }
}
