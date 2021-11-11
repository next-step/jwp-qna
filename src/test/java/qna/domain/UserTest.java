package qna.domain;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserTest {
    public static final User JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void setUp() {
        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
    }

    private static Stream<Arguments> provideUsers() {
        return Stream.of(
            Arguments.of(JAVAJIGI),
            Arguments.of(SANJIGI)
        );
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
