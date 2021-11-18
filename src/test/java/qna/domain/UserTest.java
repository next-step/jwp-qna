package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserTest {
    // data.sql을 통해 Insert 자동화
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    User actual = null;

    @BeforeEach
    void makeDefaultUser() {
        actual = userRepository.save(new User("inmookjeong", "password", "inmookjeong", "jeonginmook@gmail.com"));
    }

    @Test
    @DisplayName("회원가입 : 사용자 정보 등록")
    void signUp() {
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isEqualTo(3L),
                () -> assertThat(actual.getUserId()).isEqualTo("inmookjeong")
        );
    }

    @Test
    @DisplayName("회원정보 수정 : 회원 이름, 이메일")
    void update() {
        actual.setName("mook");
        actual.setEmail("jeonginmook2@gmail.com");
        actual.update(actual, actual);
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isEqualTo(3L),
                () -> assertThat(actual.getName()).isEqualTo("mook"),
                () -> assertThat(actual.getEmail()).isEqualTo("jeonginmook2@gmail.com")
        );
    }

    @Test
    @DisplayName("회원정보 수정 : 회원 패스워드")
    void updatePassword() {
        actual.updatePassword(actual, "newPassword");
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isEqualTo(3L),
                () -> assertThat(actual.getPassword()).isEqualTo("newPassword")
        );
    }

    @Test
    @DisplayName("전체 회원 수 조회")
    void countUsers() {
        Long count = userRepository.count();
        assertThat(count).isEqualTo(3L);
    }

    @Test
    @DisplayName("전체 회원 목록 조회")
    void getUsers() {
        String orderByColumn = "id";
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.ASC, orderByColumn));
        assertThat(users.size()).isEqualTo(3L);
        assertThat(users.get(0).getUserId()).isEqualTo("javajigi");
        assertThat(users.get(1).getUserId()).isEqualTo("sanjigi");
        assertThat(users.get(2).getUserId()).isEqualTo("inmookjeong");
    }
}
