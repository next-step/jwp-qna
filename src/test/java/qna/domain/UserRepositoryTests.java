package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("유저 저장소 테스트")
class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("유저를 저장한다.")
    void save() {
        User expected = UserTest.JAVAJIGI;
        User user = userRepository.save(expected);

        assertAll(
                () -> assertThat(user.getId()).isNotNull(),
                () -> assertThat(user.getUserId()).isEqualTo(expected.getUserId()),
                () -> assertThat(user.getPassword()).isEqualTo(expected.getPassword()),
                () -> assertThat(user.getName()).isEqualTo(expected.getName()),
                () -> assertThat(user.getEmail()).isEqualTo(expected.getEmail())
        );
    }

    @Test
    @DisplayName("유저 아이디로 유저를 조회한다.")
    void findByUserId() {
        User expected = userRepository.save(UserTest.JAVAJIGI);

        User user = userRepository.findByUserId(expected.getUserId()).get();

        assertAll(
                () -> assertThat(user.getId()).isEqualTo(expected.getId()),
                () -> assertThat(user.getUserId()).isEqualTo(expected.getUserId()),
                () -> assertThat(user.getPassword()).isEqualTo(expected.getPassword()),
                () -> assertThat(user.getName()).isEqualTo(expected.getName()),
                () -> assertThat(user.getEmail()).isEqualTo(expected.getEmail())
        );
    }
    @Test
    @DisplayName("식별자로 유저를 조회한다.")
    void findById() {
        User expected = userRepository.save(UserTest.JAVAJIGI);

        User user = userRepository.findById(expected.getId()).get();

        assertAll(
                () -> assertThat(user.getId()).isEqualTo(expected.getId()),
                () -> assertThat(user.getUserId()).isEqualTo(expected.getUserId()),
                () -> assertThat(user.getPassword()).isEqualTo(expected.getPassword()),
                () -> assertThat(user.getName()).isEqualTo(expected.getName()),
                () -> assertThat(user.getEmail()).isEqualTo(expected.getEmail())
        );
    }
    @Test
    @DisplayName("유저를 삭제한다.")
    void delete() {
        User expected = userRepository.save(UserTest.JAVAJIGI);

        userRepository.delete(expected);

        assertThat(userRepository.findById(expected.getId())).isEmpty();
    }
}
