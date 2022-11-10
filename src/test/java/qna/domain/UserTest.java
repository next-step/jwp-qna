package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import qna.config.TruncateConfig;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@EnableJpaAuditing
public class UserTest extends TruncateConfig {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    UserRepository users;


    @Test
    void 중복되는_유저아이디_save() {
        users.save(JAVAJIGI);
        User duplicationUser = new User(2L, "javajigi", "password", "name", "hello@its.me");
        assertThatThrownBy(() -> users.save(duplicationUser))
            .isInstanceOf(RuntimeException.class);
    }

    @ParameterizedTest(name = "save_테스트")
    @MethodSource("userTestFixture")
    void save_테스트(User user) {
        User saved = users.save(user);
        assertThat(saved).isEqualTo(user);
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @ParameterizedTest(name = "save_후_findById_테스트")
    @MethodSource("userTestFixture")
    void save_후_findById_테스트(User user) {
        User user1 = users.save(user);
        User user2 = users.findById(user1.getId()).get();
        assertThat(user1).isEqualTo(user2);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("rangeLimitArguments")
    void 문자열_길이_검사(String representNamed, User newUser) {
        assertThatThrownBy(() -> users.save(newUser))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    static Stream<Arguments> rangeLimitArguments() {
        Function<Integer, String> repeatString = count -> {
            final char[] single = new char[count];
            Arrays.fill(single, '아');
            return new String(single);
        };

        final int ID_LIMIT = 21;
        final int PASSWORD_LIMIT = 21;
        final int NAME_LIMIT = 21;
        final int EMAIL_LIMIT = 51;

        return Stream.of(
            Arguments.of(
                "아이디는 20자를 넘지 않는다", new User(repeatString.apply(ID_LIMIT), "password", "name", "email")
            ),
            Arguments.of(
                "패스워드는 20자를 넘지 않는다", new User("userId", repeatString.apply(PASSWORD_LIMIT), "name", "email")
            ),
            Arguments.of(
                "이름은 20자를 넘지 않는다", new User("userId", "password", repeatString.apply(NAME_LIMIT), "email")
            ),
            Arguments.of(
                "이메일은 50자를 넘지 않는다", new User("userId", "password", "name", repeatString.apply(EMAIL_LIMIT))
            )
        );
    }

    static Stream<User> userTestFixture() {
        return Stream.of(JAVAJIGI, SANJIGI);
    }
}
