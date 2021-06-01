package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @ParameterizedTest
    @MethodSource("generateData")
    void save(User user) {
        User actual = userRepository.save(user);

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getUserId()).isEqualTo(user.getUserId()),
            () -> assertThat(actual.getPassword()).isEqualTo(user.getPassword()),
            () -> assertThat(actual.getName()).isEqualTo(user.getName()),
            () -> assertThat(actual.getEmail()).isEqualTo(user.getEmail()),
            () -> assertThat(actual.getCreatedAt()).isNotNull()
        );
    }

    static List<User> generateData() {
        return Arrays.asList(JAVAJIGI,SANJIGI);
    }
}
