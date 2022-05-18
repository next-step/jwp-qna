package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class UserRepositoryTest {
    private static final String EXISTED_USER_ID = "javajigi";
    private static final String NOT_EXISTED_USER_ID = "sanjigi";

    @Autowired
    private UserRepository userRepository;

    private User EXISTED_USER;

    @BeforeEach
    void setUp() {
        EXISTED_USER = new User(EXISTED_USER_ID, "password", "name", "javajigi@slipp.net");
        userRepository.save(EXISTED_USER);
    }

    @Test
    void findByValidUserId() {
        User user = userRepository.findByUserId(EXISTED_USER_ID)
                .orElseThrow(NotFoundException::new);

        assertThat(user).isEqualTo(EXISTED_USER);
    }

    @Test
    void findByInvalidUserId() {
        assertThatThrownBy(() -> userRepository.findByUserId(NOT_EXISTED_USER_ID)
                .orElseThrow(NotFoundException::new))
                .isInstanceOf(NotFoundException.class);
    }
}
