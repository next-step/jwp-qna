package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void setup() {
        user = new User("yangId", "password", "yang", "rhfpdk92@naver.com");
        userRepository.save(user);
    }

    @Test
    void userId로_User조회() {
        User yangId = userRepository.findByUserId(user.getUserId())
                .orElseThrow(NotFoundException::new);
        assertThat(yangId).isEqualTo(user);
    }

    @Test
    void 존재하지않은_userId조회시_실패() {
        assertThatThrownBy(() -> userRepository.findByUserId("NotExistId")
                .orElseThrow(NotFoundException::new))
                .isInstanceOf(NotFoundException.class);
    }

}
