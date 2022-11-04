package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    private User JAVAJIGI;
    private User SANJIGI;

    @BeforeEach
    void setup() {
        JAVAJIGI = userRepository.save(UserTest.JAVAJIGI);
        SANJIGI = userRepository.save(UserTest.SANJIGI);
    }

    @Test
    void createdAt_updatedAt_반영_확인() {
        assertThat(JAVAJIGI.getCreatedAt()).isNotNull();
        assertThat(JAVAJIGI.getUpdatedAt()).isNotNull();
    }

    @Test
    void findByUserId() {
        Optional<User> actual = userRepository.findByUserId(JAVAJIGI.getUserId());
        assertThat(actual).isEqualTo(Optional.of(JAVAJIGI));
    }
}
