package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import qna.UnAuthorizedException;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {
    private User user;

    @BeforeEach
    void before() {
        user = new User(1L, "user1", "password", "name", "user1@com");
    }

    @Test
    void 이름과_이메일을_변경한다() {
        // given
        User loginUser = new User(1L, "user1", "password", "name", "user1@com");
        // when
        user.update(loginUser, user);
        // then
        assertThat(user.equalsNameAndEmail(loginUser)).isTrue();
    }

    @Test
    void 회원정보를_업데이트할_때_userId가_같지_않으면_예외가_발생한다() {
        // given
        User loginUser = new User("java", "pass", "mj", "mj@com");

        User target = new User();
        // when & then
        assertThatThrownBy(() ->
                user.update(loginUser, target)
        ).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 회원정보를_업데이트할_때_password가_같지_않으면_예외가_발생한다() {
        // given
        User loginUser = new User("javajigi", "pass", "mj", "mj@com");

        User target = new User("javajigi", "pass", "mj", "mj@com");
        // when & then
        assertThatThrownBy(() ->
                user.update(loginUser, target)
        ).isInstanceOf(UnAuthorizedException.class);
    }

    @ParameterizedTest
    @MethodSource(value = "user와_결과값을_리턴한다")
    void 이름과_이메일이_같은지_확인한다(User target, boolean expected) {
        // when
        boolean result = user.equalsNameAndEmail(target);
        // then
        assertThat(result).isEqualTo(expected);
    }

    static Stream<Arguments> user와_결과값을_리턴한다() {
        return Stream.of(
                Arguments.of(
                        new User("javajigi", "pass", "mj", "mj@com"),
                        false
                ),
                Arguments.of(
                        new User("user1", "password", "name", "user1@com"),
                        true
                )
        );
    }

    @Test
    void id가_같으면_동일한_유저로_판단한다() {
        // given
        User user = new User(1L,"javajigi", "pass1", "mj1", "mj@com");
        User loginUser = new User(1L, "javajigi", "pass2", "mj2", "mj@com");
        // when
        boolean result = user.match(loginUser);
        // then
        assertThat(result).isTrue();
    }
}
