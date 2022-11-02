package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.UserTest.*;

import java.util.UUID;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import qna.NoneDdlDataJpaTest;

@NoneDdlDataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager em;

    @Test
    void 유저_저장_및_찾() {
        User actual = userRepository.save(JAVAJIGI);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getUserId()).isEqualTo(JAVAJIGI.getUserId()),
            () -> assertThat(actual.getPassword()).isEqualTo(JAVAJIGI.getPassword()),
            () -> assertThat(actual.getName()).isEqualTo(JAVAJIGI.getName()),
            () -> assertThat(actual.getEmail()).isEqualTo(JAVAJIGI.getEmail()),
            () -> assertThat(actual.getCreatedAt()).isNotNull(),
            () -> assertThat(actual.getUpdatedAt()).isNotNull(),
            () -> assertThat(actual.getUpdatedAt()).isEqualTo(actual.getCreatedAt())
        );
        assertThat(userRepository.findByUserId(JAVAJIGI.getUserId())).contains(actual);
    }

    @Test
    void 유저_비밀번호_변경() {
        User user = userRepository.save(JAVAJIGI);
        String newPassword = "password2";
        user.setPassword(newPassword);
        em.flush();
        String password = userRepository.findById(user.getId()).orElseThrow(RuntimeException::new)
            .getPassword();
        assertThat(password).isEqualTo(newPassword);
    }

    @Test
    void 유저아이디는_유니크() {
        userRepository.save(JAVAJIGI);
        User sameUserIdUser = new User(JAVAJIGI.getUserId(), "password", "test@email.com",
            UUID.randomUUID().toString());
        assertThatThrownBy(() -> userRepository.save(sameUserIdUser))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

}