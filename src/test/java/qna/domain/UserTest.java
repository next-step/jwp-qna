package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    private static Stream<Arguments> provideUsers() {
        return Stream.of(
            Arguments.of(JAVAJIGI),
            Arguments.of(SANJIGI)
        );
    }

    @BeforeAll
    void setUp() {
        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
    }

    @DisplayName("User객체룰 입력으로 받는 save통하여 저장한 후 조회하면, 결과의 속성과 입력객체의 속성은 동일하다.")
    @ParameterizedTest
    @MethodSource("provideUsers")
    void saveTest(User expected) {
        User actual = userRepository.save(expected);
        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
            () -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId()),
            () -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
            () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
            () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
        );
    }

    @DisplayName("User객체룰 입력으로 받는 save통하여 저장한 후 조회하면, 결과의 속성과 입력객체의 속성은 동일하다.")
    @ParameterizedTest
    @MethodSource("provideUsers")
    void findByUserIdTest(User expected) {
        Optional<User> actual = userRepository.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(Optional.of(expected));
    }
}
