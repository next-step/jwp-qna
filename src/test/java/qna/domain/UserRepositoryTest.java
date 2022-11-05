package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.UserTest.*;

import java.util.UUID;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void 유저_저장_및_찾기() {
        User user = newUser("1");
        User actual = userRepository.save(user);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getUserId()).isEqualTo(user.getUserId()),
            () -> assertThat(actual.getPassword()).isEqualTo(user.getPassword()),
            () -> assertThat(actual.getName()).isEqualTo(user.getName()),
            () -> assertThat(actual.getEmail()).isEqualTo(user.getEmail()),
            () -> assertThat(actual.getCreatedAt()).isNotNull(),
            () -> assertThat(actual.getUpdatedAt()).isNotNull(),
            () -> assertThat(actual.getUpdatedAt()).isEqualTo(actual.getCreatedAt())
        );
        assertThat(userRepository.findByUserId(user.getUserId())).contains(actual);
    }

    @Test
    void 유저_비밀번호_변경() {
        User user = userRepository.save(newUser("1"));
        String newPassword = "password2";
        user.setPassword(newPassword);
        String password = userRepository.findById(user.getId()).orElseThrow(RuntimeException::new)
            .getPassword();
        assertThat(password).isEqualTo(newPassword);
    }

    @Test
    void 유저아이디는_유니크() {
        User savedUser = userRepository.save(newUser("1"));
        User sameUserIdUser = new User(savedUser.getUserId(), "password", "test@email.com",
            UUID.randomUUID().toString());
        assertThatThrownBy(() -> userRepository.save(sameUserIdUser))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void 유저아이디_null_validation() {
        User user = new User(null, "password", "name", "test@email.com");
        assertThatThrownBy(() -> userRepository.save(user))
            .isInstanceOf(ConstraintViolationException.class);
    }

}