package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.user.User;
import qna.domain.user.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserTest {
    public static final User JAVAJIGI = UserTestFactory.create("javajigi", "javajigi@slipp.net");
    public static final User SANJIGI = UserTestFactory.create("sanjigi", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("User 를 저장 할 경우 저장된 객체와 저장 후 객체가 일치하다")
    void save() {
        final User user = UserTestFactory.create("doyoung", "doyoung@qna.test");
        final User savedUser = userRepository.save(user);
        assertEquals(savedUser, user);
    }

    @Test
    @DisplayName("아이디를 통해서 User를 조회할 수 있다")
    void findByUserId() {
        // given
        final User savedUser = userRepository.save(UserTestFactory.create("doyoung", "doyoung@qna.test"));
        // when
        final Optional<User> userOptional = userRepository.findByUserId(savedUser.getUserId());
        // then
        assertAll(() -> {
            assertTrue(userOptional.isPresent());
            assertEquals(userOptional.get(), savedUser);
        });
    }

    @Test
    @DisplayName("이메일과 이름을 변경 할 수 있다.")
    void update() {
        // given
        final User savedUser = userRepository.save(UserTestFactory.create("doyoung", "doyoung@qna.test"));
        final User newUser = UserTestFactory.create(savedUser.getId(), savedUser.getUserId(), "newName", "newEmail@email.com");
        savedUser.update(newUser);
        // when
        final Optional<User> userOptional = userRepository.findByUserId(newUser.getUserId());
        // then
        assertAll(() -> {
            assertTrue(userOptional.isPresent());
            assertTrue(userOptional.get().matchEmail("newEmail@email.com"));
            assertTrue(userOptional.get().matchName("newName"));
        });

    }
}
