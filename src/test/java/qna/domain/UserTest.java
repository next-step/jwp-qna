package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserTest {
    // data.sql을 통해 Insert 자동화
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원가입 : 사용자 정보 등록")
    void signUp() {
        User actual = userRepository.save(new User("inmookjeong", "password", "inmookjeong", "jeonginmook@gmail.com"));
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isEqualTo(3L),
                () -> assertThat(actual.getUserId()).isEqualTo("inmookjeong")
        );
    }

    @Test
    @DisplayName("회원정보 수정")
    void update() {
        User actual = userRepository.save(new User("inmookjeong", "password", "inmookjeong", "jeonginmook@gmail.com"));
        actual.setName("mook");
        actual.update(actual, actual);
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isEqualTo(3L),
                () -> assertThat(actual.getName()).isEqualTo("mook")
        );
    }
}
