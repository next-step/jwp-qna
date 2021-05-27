package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("DB에 저장된 Entity와 저장하기전 Entity가 동일한지 확인")
    @Test
    void insertTest() {
        User user = new User("dacapolife87", "password", "name", "dacapolife87@gmail.com");
        User savedUser = userRepository.save(user);

        User findUser = userRepository.getOne(savedUser.getId());

        assertThat(savedUser).isEqualTo(findUser);
    }

    @DisplayName("사용자ID를 통하여 사용자조회 테스트")
    @Test
    void findByUserIdTest() {
        User user = new User("dacapolife87", "password", "name", "dacapolife87@gmail.com");
        User savedUser = userRepository.save(user);

        Optional<User> findUser = userRepository.findByUserId(user.getUserId());

        assertAll(
                () -> assertThat(findUser).isNotEmpty(),
                () -> assertTrue(findUser.isPresent()),
                () -> assertThat(findUser.get()).isEqualTo(savedUser)
        );
    }

    @DisplayName("사용자 이메일 업데이트 테스트")
    @Test
    void updateTest() {
        User user = new User("dacapolife87", "password", "name", "dacapolife87@gmail.com");
        User savedUser = userRepository.save(user);

        String newEmail = "dacapolife87@naver.com";
        savedUser.setEmail(newEmail);

        userRepository.flush();

        User findUser = userRepository.getOne(savedUser.getId());

        assertThat(findUser.getEmail()).isEqualTo(newEmail);
    }
}
