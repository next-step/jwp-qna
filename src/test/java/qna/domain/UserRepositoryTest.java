package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    
    @BeforeEach
    void setUp() {
        userRepository.deleteAllInBatch();
    }
    
    @Test
    void save() {
        User userTest = UserTest.JAVAJIGI;
        User user = userRepository.save(userTest);
        assertThat(user.getUserId()).isEqualTo(userTest.getUserId());
    }
}
