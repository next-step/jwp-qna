package qna.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, UserId.of("javajigi"), Password.of("password"), Name.of("name"), Email.of("javajigi@slipp.net"));
    public static final User SANJIGI = new User(2L, UserId.of("sanjigi"), Password.of("password"), Name.of("name"), Email.of("sanjigi@slipp.net"));

    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("사용자를 생성할 수 있어야 한다.")
    void create_user() {
        User user1 = new User(UserId.of("test"), Password.of("password"), Name.of("test1"), Email.of("test@test.net"));

        // 이메일 형식 체크
        assertThatThrownBy(() ->
                new User(UserId.of("test2"), Password.of("password"), Name.of("test2"), Email.of("te@st@test.net"))
        ).isInstanceOf(IllegalArgumentException.class);

        // 빈값 체크
        assertThatThrownBy(() ->
                new User(UserId.of("test"), Password.of("password"), Name.of(""), Email.of("test@test.net"))
        ).isInstanceOf(IllegalArgumentException.class);


        assertAll(
                () -> assertThat(user1.getUserId()).isEqualTo(UserId.of("test")),
                () -> assertThat(user1.getPassword()).isEqualTo(Password.of("password")),
                () -> assertThat(user1.getName()).isEqualTo(Name.of("test1")),
                () -> assertThat(user1.getEmail()).isEqualTo(Email.of("test@test.net"))
        );
    }
}
