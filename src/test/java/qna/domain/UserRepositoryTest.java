package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.UserTest.*;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager em;

    private User savedUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAllInBatch();
        this.savedUser = userRepository.save(newUser("1"));
    }

    @Test
    void 유저_저장_및_찾기() {
        User user = newUser("1");
        assertAll(
            () -> assertThat(savedUser.getId()).isNotNull(),
            () -> assertThat(savedUser.getUserId()).isEqualTo(user.getUserId()),
            () -> assertThat(savedUser.getPassword()).isEqualTo(user.getPassword()),
            () -> assertThat(savedUser.getName()).isEqualTo(user.getName()),
            () -> assertThat(savedUser.getEmail()).isEqualTo(user.getEmail()),
            () -> assertThat(savedUser.getCreatedAt()).isNotNull(),
            () -> assertThat(savedUser.getUpdatedAt()).isNotNull(),
            () -> assertThat(savedUser.getUpdatedAt()).isEqualTo(savedUser.getCreatedAt())
        );
        assertThat(userRepository.findByUserId(user.getUserId())).contains(savedUser);
    }

    @Test
    void 유저_비밀번호_변경() {
        String newPassword = "password2";
        savedUser.setPassword(newPassword);
        em.flush();
        assertThat(userRepository.findById(savedUser.getId()))
            .get()
            .extracting(User::getPassword)
            .isEqualTo(newPassword);
    }

    @Test
    void 유저아이디는_유니크() {
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