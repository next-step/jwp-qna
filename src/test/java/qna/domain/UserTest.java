package qna.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import qna.UnAuthorizedException;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    void 이름과_이메일을_변경한다() {
        // given
        User loginUser = new User("javajigi", "pass", "mj", "mj@com");

        User target = new User("javajigi", "password", "mj", "mj@com");
        // when
        JAVAJIGI.update(loginUser, target);
        // then
        assertThat(JAVAJIGI.equalsNameAndEmail(target)).isTrue();
    }

    @Test
    void 회원정보를_업데이트할_때_userId가_같지_않으면_예외가_발생한다() {
        // given
        User loginUser = new User("java", "pass", "mj", "mj@com");

        User target = new User();
        // when & then
        assertThatThrownBy(() ->
            JAVAJIGI.update(loginUser, target)
        ).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 회원정보를_업데이트할_때_password가_같지_않으면_예외가_발생한다() {
        // given
        User loginUser = new User("javajigi", "pass", "mj", "mj@com");

        User target = new User("javajigi", "pass", "mj", "mj@com");
        // when & then
        assertThatThrownBy(() ->
                JAVAJIGI.update(loginUser, target)
        ).isInstanceOf(UnAuthorizedException.class);
    }

    @ParameterizedTest
    @MethodSource
    void 이름과_이메일이_같은지_확인한다(User target, boolean expected) {
        // when
        boolean result = JAVAJIGI.equalsNameAndEmail(target);
        // then
        assertThat(result).isEqualTo(expected);
    }

    static Stream<Arguments> 이름과_이메일이_같은지_확인한다() {
        return Stream.of(
                Arguments.of(
                        new User("javajigi", "pass", "mj", "mj@com"),
                        false
                ),
                Arguments.of(
                        new User("javajigi", "pass","name", "javajigi@slipp.net"),
                        true
                )
        );
    }


}
