package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void clear() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("유저를 저장한다.")
    void save() {
        User savedUser = userRepository.save(JAVAJIGI);
        User foundUser = userRepository.getOne(savedUser.getId());

        assertThat(savedUser)
                .isNotNull()
                .isEqualTo(foundUser);
    }

    @Test
    @DisplayName("유저ID로 유저를 조회한다.")
    void findByUserId() {
        User savedUser = userRepository.save(JAVAJIGI);
        Optional<User> foundUser = userRepository.findByUserId(savedUser.getUserId());

        assertThat(foundUser)
                .isNotEmpty()
                .hasValueSatisfying(user -> assertThat(user).isEqualTo(savedUser));
    }

}
