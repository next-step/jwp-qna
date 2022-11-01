package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    public User userTest;

    @BeforeEach
    void setUp() {
        userTest = UserTest.JAVAJIGI;
    }

    @Test
    @DisplayName("User 저장 테스트")
    void save() {
        User user = userRepository.save(userTest);
        assertThat(user.toString()).isEqualTo(userTest.toString());
    }
}
