package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void before() {
        userRepository.deleteAll();
    }

    @DisplayName("User 저장 테스트")
    @Test
    void saveTest() {
        User expected = UserTest.JAVAJIGI;
        User result = userRepository.save(expected);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertTrue(result.equalsNameAndEmail(expected));
    }

    @DisplayName("User 조회 테스트 / userId로 조회")
    @Test
    void findTest() {
        User expected = userRepository.save(UserTest.SANJIGI);
        Optional<User> resultOptional = userRepository.findByUserId(expected.getUserId());
        assertThat(resultOptional).isNotEmpty();

        User result = resultOptional.get();
        assertThat(result.getId()).isEqualTo(expected.getId());
        assertThat(result.getUserId()).isEqualTo(expected.getUserId());
        assertTrue(result.equalsNameAndEmail(expected));
    }
}