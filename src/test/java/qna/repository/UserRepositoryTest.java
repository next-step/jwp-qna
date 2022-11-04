package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;
import qna.domain.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository users;
    private User loginUser;
    private User targetUser;

    @BeforeEach
    void setUp() {
        loginUser = new User("seungki1993", "pass1", "seungki", "test@Naver.com");
        targetUser = new User("target", "pass1", "target", "target@Naver.com");
    }

    @Test
    @DisplayName("user 저장")
    void save_user() {
        User actual = users.save(loginUser);
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(loginUser.getId()),
                () -> assertThat(actual.getUserId()).isEqualTo(loginUser.getUserId()),
                () -> assertThat(actual.getName()).isEqualTo(loginUser.getName()),
                () -> assertThat(actual.getEmail()).isEqualTo(loginUser.getEmail()),
                () -> assertThat(actual.getPassword()).isNotNull().isEqualTo(loginUser.getPassword())
        );
    }

    @Test
    @DisplayName("user id로 조회")
    void user_find_by_id() {
        User saveUser = users.save(loginUser);
        User actual = users.findByUserId(saveUser.getUserId()).orElseThrow(NotFoundException::new);
        assertThat(saveUser).isEqualTo(actual);
    }

    @Test
    @DisplayName("user 전체 조회")
    void find_user_all() {
        users.save(loginUser);
        users.save(targetUser);
        List<User> saveUsers = users.findAll();
        assertThat(saveUsers).hasSize(2);
    }

    @Test
    @DisplayName("user 삭제")
    void delete_user() {
        User actual = users.save(loginUser);
        assertThat(users.findByUserId(loginUser.getUserId())).isNotEmpty();
        users.delete(actual);
        assertThat(users.findByUserId(loginUser.getUserId())).isEmpty();
    }

    @Test
    @DisplayName("user 이름 이메일 업데이트")
    void update_name_email() {
        User expect = users.save(loginUser);
        expect.update(expect, targetUser);
        User actual = users.findByUserId(expect.getUserId()).orElseThrow(NotFoundException::new);
        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(targetUser.getName()),
                () -> assertThat(actual.getEmail()).isEqualTo(targetUser.getEmail())
        );
    }
}
