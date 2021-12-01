package qna.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class UserTest {

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @ParameterizedTest
    @NullAndEmptySource
    void user_id_null_테스트(String userId) {
        assertThatIllegalArgumentException().isThrownBy(() -> new User(1L, userId, "password", "name", "javajigi@slipp.net"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void password_null_테스트(String password) {
        assertThatIllegalArgumentException().isThrownBy(() -> new User(1L, "javajigi", password, "name", "javajigi@slipp.net"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void name_null_테스트(String name) {
        assertThatIllegalArgumentException().isThrownBy(() -> new User(1L, "javajigi", "password", name, "javajigi@slipp.net"));
    }
}
