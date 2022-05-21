package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("userId로 User를 조회한다.")
    @Test
    void findByUserId() {
        //given
        final User expected = userRepository.save(UserTest.JAVAJIGI);

        //when
        final Optional<User> actual = userRepository.findByUserId(expected.getUserId());

        //then
        assertAll(
                () -> assertThat(actual.isPresent()).isTrue(),
                () -> assertThat(actual.get()).isSameAs(expected)
        );
    }

    @ParameterizedTest(name = "userId/password/name/email의 길이는 지정된 length까지만 허용된다.")
    @MethodSource("createInvalidUsers")
    void save_invalid_length(User user) {
        assertThatThrownBy(() -> userRepository.save(user))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    private static Stream<Arguments> createInvalidUsers() {
        return Stream.of(
                Arguments.of(new User("abcdefghijklmnopqrstuvwxyz", "password", "name", "email")),
                Arguments.of(new User("userid", "abcdefghijklmnopqrstuvwxyz", "name", "email")),
                Arguments.of(new User("userid", "password", "abcdefghijklmnopqrstuvwxyz", "email")),
                Arguments.of(new User("userid", "password", "name",
                        "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"))
        );
    }

    @DisplayName("모든 User를 조회한다.")
    @Test
    void findAll() {
        //given
        final User user1 = userRepository.save(UserTest.JAVAJIGI);
        final User user2 = userRepository.save(UserTest.SANJIGI);

        //when
        final List<User> users = userRepository.findAll();

        //then
        assertAll(
                () -> assertThat(users).hasSize(2),
                () -> assertThat(users).contains(user1),
                () -> assertThat(users).contains(user2)
        );
    }
}