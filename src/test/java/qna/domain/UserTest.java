package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name1", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name2", "sanjigi@slipp.net");

    @DisplayName("User 객체 기본 API 테스트")
    @Test
    void userNormal() {
        assertAll(
                () -> assertThat(JAVAJIGI.matchPassword("password")).isTrue(),
                () -> assertThat(JAVAJIGI.matchPassword("abcdefgh")).isFalse(),
                () -> assertThat(SANJIGI.equalsNameAndEmail(JAVAJIGI)).isFalse()
        );
    }

    @DisplayName("User 객체 update 테스트")
    @Test
    void updateUser() {
        User user = new User(3L, "baejigi", "password", "name3", "javajigi@slipp.net");
        user.update(user, SANJIGI);

        assertAll(
                () -> assertThat(user.getName()).isEqualTo("name2"),
                () -> assertThat(user.getEmail()).isEqualTo("sanjigi@slipp.net")
        );
    }

}
