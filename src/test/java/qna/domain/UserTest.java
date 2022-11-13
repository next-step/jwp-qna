package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import qna.config.JpaAuditingConfiguration;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(JpaAuditingConfiguration.class)
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    UserRepository users;


    User user;

    @BeforeEach
    void setUp() {
        user = users.save(new User("userId", "password", "name", "email"));
    }

    @Test
    void 중복되는_유저아이디_save() {
        User duplicationUser = new User(2L, "userId", "password", "name", "hello@its.me");
        assertThatThrownBy(() -> users.save(duplicationUser))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    void save_테스트() {
        assertThat(user.getId()).isNotNull();
        assertThat(user.getCreatedAt()).isNotNull();
    }

    @Test
    void save_후_findById_테스트() {
        Optional<User> maybe = users.findById(user.getId());
        assertThat(maybe.isPresent()).isTrue();
        assertThat(maybe.get()).isEqualTo(user);
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
}
