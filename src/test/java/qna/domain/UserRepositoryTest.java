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
    private UserRepository userRepository;

    private static final String USER_ID = "alice";
    private static final String PASSWORD = "password";
    private static final String NAME = "Alice";
    private static final String EMAIL = "alice@mail";

    @DisplayName("UserId로 찾기")
    @Test
    void findByUserId() {
        User alice = new User(USER_ID, PASSWORD, NAME, EMAIL);
        userRepository.save(alice);

        User actual = userRepository
            .findByUserId(alice.getUserId())
            .orElseThrow(NotFoundException::new);

        assertThat(actual).isSameAs(alice);
    }

    @DisplayName("저장된 사용자의 UserId 수정하기")
    @ParameterizedTest(name = "새 UserId: '{0}'")
    @ValueSource(strings = {"New User Id"})
    void update(String expected) {
        User alice = new User(USER_ID, PASSWORD, NAME, EMAIL);
        userRepository.save(alice);

        alice.setUserId(expected);

        User actual = userRepository
            .findByUserId(expected)
            .orElseThrow(NotFoundException::new);

        assertThat(actual.getUserId()).isEqualTo(expected);
    }

    @DisplayName("사용자 정보를 수정한 시각을 기록하기")
    @ParameterizedTest(name = "새 UserId: '{0}'")
    @ValueSource(strings = {"New User Id"})
    void updateUpdatedAt(String expected) {
        User alice = new User(USER_ID, PASSWORD, NAME, EMAIL);
        userRepository.save(alice);

        assertThat(alice.getUpdatedAt()).isEqualTo(alice.getCreatedAt());

        alice.setUserId(expected);
        userRepository.flush();

        assertThat(alice.getUpdatedAt()).isNotEqualTo(alice.getCreatedAt());
    }

    @DisplayName("삭제하기")
    @Test
    void delete() {
        User alice = new User(USER_ID, PASSWORD, NAME, EMAIL);
        userRepository.save(alice);

        userRepository.delete(alice);

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() ->
            userRepository.findById(alice.getId()).get()
        );
    }

}
