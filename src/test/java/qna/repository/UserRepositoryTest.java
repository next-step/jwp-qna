package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import qna.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAllInBatch();
    }

    @Test
    void save() {
        User user = userRepository.save(JAVAJIGI);
        assertAll(
                () -> assertThat(user.getUserId()).isEqualTo(JAVAJIGI.getUserId()),
                () -> assertThat(user.getEmail()).isEqualTo(JAVAJIGI.getEmail())
        );
    }

    @Test
    void save_retreive_test() {
        // given
        User saveUser = userRepository.save(JAVAJIGI);
        // when
        Optional<User> findUser = userRepository.findByUserId(saveUser.getUserId());
        // then
        assertThat(findUser).isPresent();
        findUser.ifPresent(user -> assertAll(
                () -> assertThat(user).isEqualTo(saveUser),
                () -> assertThat(user.getUserId()).isEqualTo(saveUser.getUserId())
        ));
    }

    @Test
    void duplicate_userId_test() {
        userRepository.save(SANJIGI);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> userRepository.save(SANJIGI)).withMessageContaining("constraint");
    }

    @Test
    void user_auto_id_strategy_test() {
        // given
        User user = new User("deokmoon", "password", "name", "deokmoon@nextstep.com");
        // when
        User saveUser = userRepository.save(user);
        // then
        assertThat(saveUser.getId()).isNotNull();
    }

    @Test
    void user_delete_test() {
        // given
        User saveUser = userRepository.save(SANJIGI);
        Long saveUserId = saveUser.getId();
        Optional<User> findUser = userRepository.findById(saveUserId);
        // when
        findUser.ifPresent(user -> userRepository.delete(user));
        Optional<User> deletedUser = userRepository.findById(saveUserId);
        // then
        assertThat(deletedUser).isNotPresent();
    }
}
