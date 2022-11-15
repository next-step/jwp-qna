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
        User writer = User.create("javajigi");
        User user = userRepository.save(writer);
        assertAll(
                () -> assertThat(user.getUserId()).isEqualTo(writer.getUserId()),
                () -> assertThat(user.getEmail()).isEqualTo(writer.getEmail())
        );
    }

    @Test
    void save_retreive_test() {
        // given
        User writer = User.create("javajigi");
        User saveUser = userRepository.save(writer);
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
        User writer = User.create("sagjigi");
        userRepository.save(writer);
        User dulpicateUser = User.create("sagjigi");

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> userRepository.save(dulpicateUser)).withMessageContaining("constraint");
    }

    @Test
    void user_auto_id_strategy_test() {
        // given
        User user = User.create("deokmoon");
        // when
        User saveUser = userRepository.save(user);
        // then
        assertThat(saveUser.getId()).isNotNull();
    }

    @Test
    void user_delete_test() {
        // given
        User writer = User.create("sagjigi");
        User saveUser = userRepository.save(writer);
        Long saveUserId = saveUser.getId();
        Optional<User> findUser = userRepository.findById(saveUserId);
        // when
        findUser.ifPresent(user -> userRepository.delete(user));
        Optional<User> deletedUser = userRepository.findById(saveUserId);
        // then
        assertThat(deletedUser).isNotPresent();
    }
}
