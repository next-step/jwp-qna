package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository users;

    String userId;
    User user;

    @BeforeEach
    void setUp() {
        userId = "testUser";
        user = users.save(new User(userId, "qwerty1234", "김철수", "testUser@nextstep.com"));
    }

    @DisplayName("user 저장 확인")
    @Test
    void save() {
        String userId = "testUser2";
        final User user2 = users.save(new User(userId, "qwerty12345", "김영희", "testUser2@nextstep.com"));
        assertAll(
                () -> assertThat(user2.getId()).isNotNull(),
                () -> assertThat(user2.getUserId()).isEqualTo(userId)
        );
    }

    @DisplayName("user 테이블 userId로 row select 테스트")
    @Test
    void findByUserId() {
        final User actual = users.findByUserId(userId).get();
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getUserId()).isEqualTo(userId)
        );
    }

    @DisplayName("user 테이블 password 수정 테스트")
    @Test
    void update_password() {
        String expected = "asdf1234";
        user.setPassword(expected);
        final User actual = users.findByUserId(user.getUserId()).get();
        assertAll(
                () -> assertThat(user.getUserId()).isNotNull(),
                () -> assertThat(actual.getPassword()).isEqualTo(expected)
        );
    }

    @DisplayName("user 테이블 name 수정 테스트")
    @Test
    void update_name() {
        String expected = "김철수2";
        user.setName(expected);
        final User actual = users.findByUserId(user.getUserId()).get();
        assertAll(
                () -> assertThat(user.getUserId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected)
        );
    }

    @DisplayName("user 테이블 email 수정 테스트")
    @Test
    void update_email() {
        String expected = "cheolsu@nextstep.com";
        user.setEmail(expected);
        final User actual = users.findByUserId(user.getUserId()).get();
        assertAll(
                () -> assertThat(user.getUserId()).isNotNull(),
                () -> assertThat(actual.getEmail()).isEqualTo(expected)
        );
    }

    @DisplayName("user 삭제 테스트")
    @Test
    void delete() {
        users.delete(user);
        assertAll(
                () -> assertThat(user.getUserId()).isNotNull(),
                () -> assertThat(users.findByUserId(user.getUserId())).isEmpty()
        );
    }

}