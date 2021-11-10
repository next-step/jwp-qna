package qna.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.saveAll(Arrays.asList(JAVAJIGI, SANJIGI));
    }

    @Test
    void updateEmail() {
        // given
        Optional<User> byUserId = userRepository.findByUserId(JAVAJIGI.getUserId());
        User user = byUserId.get();

        // when
        final String updateEmail = "seunghoo@naver.com";
        user.setEmail(updateEmail);

        // then
        Assertions.assertThat(user.getEmail()).isEqualTo(updateEmail);
    }
}
