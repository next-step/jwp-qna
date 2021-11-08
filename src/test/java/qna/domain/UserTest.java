package qna.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    private static Stream<Arguments> providerUsers() {
        return Stream.of(
                Arguments.of(JAVAJIGI),
                Arguments.of(SANJIGI)
        );
    }

    @ParameterizedTest
    @MethodSource("providerUsers")
    public void 유저_생성(User excepted) {
        User actual = userRepository.save(excepted);

        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(excepted.getId()),
                () -> assertThat(actual.getEmail()).isEqualTo(excepted.getEmail()),
                () -> assertThat(actual.getUserId()).isEqualTo(excepted.getUserId()),
                () -> assertThat(actual.getName()).isEqualTo(excepted.getName()),
                () -> assertThat(actual.getPassword()).isEqualTo(excepted.getPassword())
        );
    }

}
