package qna.domain;

import static qna.domain.UserTest.JAVAJIGI;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void 사용자_조회() {
        User savedUser = userRepository.save(JAVAJIGI);
        User user = userRepository.findByUserId(JAVAJIGI.getUserId())
            .orElse(null);
        Assertions.assertThat(savedUser).isEqualTo(user);
    }

}