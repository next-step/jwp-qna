package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.exceptions.NotFoundException;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository users;

    private static final String USER_ID = "alice";
    private static final String PASSWORD = "password";
    private static final String NAME = "Alice";
    private static final String EMAIL = "alice@mail";

    @DisplayName("UserId로 찾기")
    @Test
    void findByUserId() {
        User alice = new User(USER_ID, PASSWORD, NAME, EMAIL);
        users.save(alice);

        User actual = users
            .findByUserId(alice.getUserId())
            .orElseThrow(NotFoundException::new);

        assertThat(actual).isSameAs(alice);
    }

    @DisplayName("저장된 사용자의 UserId 수정하기")
    @ParameterizedTest(name = "새 UserId: '{0}'")
    @ValueSource(strings = {"New User Id"})
    void update(String expected) {
        User alice = new User(USER_ID, PASSWORD, NAME, EMAIL);
        users.save(alice);

        alice.setUserId(expected);

        User actual = users
            .findByUserId(expected)
            .orElseThrow(NotFoundException::new);

        assertThat(actual.getUserId()).isEqualTo(expected);
    }

    @DisplayName("사용자 정보를 수정한 시각을 기록하기")
    @ParameterizedTest(name = "새 UserId: '{0}'")
    @ValueSource(strings = {"New User Id"})
    void updateUpdatedAt(String expected) {
        User alice = new User(USER_ID, PASSWORD, NAME, EMAIL);
        users.save(alice);

        assertThat(alice.getUpdatedAt()).isNull();

        alice.setUserId(expected);
        users.flush();

        assertThat(alice.getUpdatedAt()).isNotNull();
    }

    @DisplayName("삭제하기")
    @Test
    void delete() {
        User alice = new User(USER_ID, PASSWORD, NAME, EMAIL);
        users.save(alice);

        users.delete(alice);

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() ->
            users.findById(alice.getId()).get()
        );
    }

}
