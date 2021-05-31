package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserTest {

    private User user1;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        user1 = new User("javajigi", "password", "name", "javajigi@slipp.net");
    }

    @Test
    @DisplayName("사용자 정보 저장")
    void save() {
        User result = new User("javajigi", "password", "name", "javajigi@slipp.net");
        userRepository.save(result);
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getName()).isEqualTo(user1.getName()),
                () -> assertThat(result.getUserId()).isEqualTo(user1.getUserId()),
                () -> assertThat(result.getPassword()).isEqualTo(user1.getPassword()),
                () -> assertThat(result.getEmail()).isEqualTo(user1.getEmail())
        );
    }

    @Test
    @DisplayName("ID 로 정보조회")
    void findById() {
        User user = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        userRepository.save(user);
        User result = userRepository.findById(user.getId()).get();
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getEmail()).isEqualTo(user.getEmail()),
                () -> assertThat(result.getUserId()).isEqualTo(user.getUserId()),
                () -> assertThat(result.getPassword()).isEqualTo(user.getPassword()),
                () -> assertThat(result.getName()).isEqualTo(user.getName())
        );
    }

    @Test
    @DisplayName("사용자ID로 사용자 정보 조회")
    void findByUserId() {
        User user = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        userRepository.save(user);
        User result = userRepository.findByUserId(user.getUserId()).get();
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getPassword()).isEqualTo(user.getPassword()),
                () -> assertThat(result.getEmail()).isEqualTo(user.getEmail()),
                () -> assertThat(result.getUserId()).isEqualTo(user.getUserId()),
                () -> assertThat(result.getName()).isEqualTo(user.getName())
        );
    }

    @Test
    @DisplayName("사용자 삭제 테스트")
    void delete() {
        User user = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        userRepository.save(user);
        userRepository.deleteById(user.getId());

        assertThat(userRepository.findByUserId(user.getUserId())).isNotPresent();
    }
}