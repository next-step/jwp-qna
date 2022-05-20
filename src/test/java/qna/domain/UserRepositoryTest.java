package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("저장이 잘 되는지 테스트")
    void save() {
        User expected = new User("yulmucha", "1234", "Yul", "yul@google.com");
        User actual = userRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId()),
                () -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
        );
    }

    @Test
    @DisplayName("userId로 잘 찾아지는지 테스트")
    void findByUserId() {
        String expected = "yulmucha";
        userRepository.save(new User(expected, "1234", "yul", "yul@google.com"));
        String actual = userRepository.findByUserId(expected).get().getUserId();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("개체를 저장한 후 다시 가져왔을 때 기존의 개체와 동일한지 테스트")
    void findById() {
        User user = new User("yulmucha", "1234", "yul", "yul@google.com");
        User savedUser = userRepository.save(user);

        User foundUser = userRepository.findById(savedUser.getId()).get();
        assertThat(foundUser).isEqualTo(user);
    }
}
