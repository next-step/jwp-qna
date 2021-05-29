package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository users;

    private User savedUser;

    @BeforeEach
    void init() {
        savedUser = users.save(UserTest.JAVAJIGI);
    }

    @Test
    @DisplayName("저장 테스트")
    void save() {
        // given & when & then
        assertAll(
            () -> assertThat(savedUser.getId()).isNotNull(),
            () -> assertThat(savedUser.getUserId()).isEqualTo(UserTest.JAVAJIGI.getUserId()),
            () -> assertThat(savedUser.getName()).isEqualTo(UserTest.JAVAJIGI.getName()),
            () -> assertThat(savedUser.getPassword()).isEqualTo(UserTest.JAVAJIGI.getPassword()),
            () -> assertThat(savedUser.getEmail()).isEqualTo(UserTest.JAVAJIGI.getEmail())
        );
    }

    @Test
    @DisplayName("영속성 동일성 테스트")
    void findById() {
        // given & when
        User actual = users.findById(savedUser.getId()).get();
        // then
        assertThat(actual).isEqualTo(savedUser);
    }

    @Test
    @DisplayName("수정 전 테스트")
    void update_before() {
        // given
        String newEmail = "ehdgml3206@gmail.com";
        // when & then
        assertThat(savedUser.getEmail()).isNotEqualTo(newEmail);
    }

    @Test
    @DisplayName("수정 테스트")
    void update() {
        // given
        String newEmail = "ehdgml3206@gmail.com";
        // when
        savedUser.setEmail(newEmail);
        users.flush();
        User actual = users.findById(savedUser.getId()).get();
        // then
        assertThat(actual.getEmail()).isEqualTo(newEmail);
    }

    @Test
    @DisplayName("삭제 테스트")
    void delete() {
        // given & when
        users.delete(savedUser);
        // then
        assertThat(users.findById(savedUser.getId())).isNotPresent();
    }

    @Test
    @DisplayName("유저 Id로 조회 테스트")
    void findByUserId() {
        // given & when
        Optional<User> actual = users.findByUserId(savedUser.getUserId());
        // then
        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(savedUser);
    }
}
