package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.UserTest.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import qna.NotFoundException;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void user_save_and_find() {
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
        assertThat(userRepository.findByUserId(JAVAJIGI.getUserId()).orElseThrow(RuntimeException::new))
            .isEqualTo(actual);
    }

    @Test
    void user_update() {
        User user = userRepository.save(JAVAJIGI);
        String newPassword = UUID.randomUUID().toString();
        user.setPassword(newPassword);
        User actual = userRepository.findByUserId(user.getUserId())
            .orElseThrow(RuntimeException::new);
        assertThat(actual.getPassword()).isEqualTo(newPassword);
    }

    @Test
    void userId_unique() {
        userRepository.save(JAVAJIGI);
        User sameUserIdUser = new User(JAVAJIGI.getUserId(), UUID.randomUUID().toString(), UUID.randomUUID().toString(),
            UUID.randomUUID().toString());
        assertThatThrownBy(() -> userRepository.save(sameUserIdUser))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void not_null_필드를_null_로_생성할경우_예외처리한다() {
        assertThatThrownBy(() -> new User(null, UUID.randomUUID().toString(), "name", "email@test.com"))
            .isInstanceOf(NotFoundException.class);
    }

}