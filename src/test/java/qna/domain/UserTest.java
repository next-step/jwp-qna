package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    }

    @Test
    @DisplayName("생성 테스트")
    void create() {
        assertThat(user).isEqualTo(new User(1L, "javajigi", "password", "name", "javajigi@slipp.net"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("User 의 name 업데이트 시 빈 값 입력 하면 예외발생")
    void validateUpdateName(String name) {
        assertThatThrownBy(() -> user.updateName(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("null 또는 빈값으로 name을 업데이트 할 수 없습니다.");
    }

    @Test
    @DisplayName("User 의 name 정상 업데이트 테스트")
    void updateName() {
        String changedName = "변경된 이름";

        user.updateName(changedName);

        assertThat(user.getName()).isEqualTo(changedName);
    }
}
