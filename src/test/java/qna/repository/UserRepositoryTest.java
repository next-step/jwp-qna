package qna.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @DisplayName("유저를 저장할 수 있다")
    @Test
    void save_test() {
        User saved = userRepository.save(UserTest.JAVAJIGI);

        assertAll(
                () -> assertNotNull(saved.getId()),
                () -> assertEquals(saved.getUserId(), UserTest.JAVAJIGI.getUserId()),
                () -> assertEquals(saved.getPassword(), UserTest.JAVAJIGI.getPassword()),
                () -> assertEquals(saved.getName(), UserTest.JAVAJIGI.getName()),
                () -> assertEquals(saved.getEmail(), UserTest.JAVAJIGI.getEmail())
        );
    }

    @DisplayName("유저를 저장할 때 user_id가 중복되면 DataIntegrityViolationException 발생")
    @Test
    void save_exception() {
        userRepository.save(UserTest.JAVAJIGI);

        assertThatThrownBy(() -> userRepository.save(UserTest.JAVAJIGI))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @DisplayName("유저의 유저아이디로 동일한 유저를 조회할 수 있다.")
    @Test
    void findByUserId_test() {
        User saved = userRepository.save(UserTest.JAVAJIGI);

        Optional<User> user1 = userRepository.findById(saved.getId());

        Optional<User> user2 = userRepository.findByUserId(saved.getUserId());

        assertAll(
                () -> assertTrue(user2.isPresent()),
                () -> assertEquals(user1, user2)
        );
    }

}