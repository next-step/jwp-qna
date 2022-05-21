package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;
import qna.domain.UserRepository;
import qna.domain.UserTest;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository users;

    @Test
    @DisplayName("저장")
    void create() {
        User savedUser = users.save(UserTest.JAVAJIGI);
        Optional<User> actual = users.findById(savedUser.getId());
        assertThat(actual).isPresent();
    }

    @Test
    @DisplayName("없는 id 조회")
    void findNotExistAnswer() {
        User user = new User(3L, "sykim", "password", "name", "sykim@sykim.com");
        users.save(user);
        Optional<User> actual = users.findById(9999L);
        assertThat(actual).isNotPresent();
    }

    @Test
    @DisplayName("갱신")
    void update() {
        final String newPassword = "newPassword";
        User user = new User(3L, "sykim", "password", "name", "sykim@sykim.com");
        User actual = users.save(user);
        assertThat(actual.getUserId()).isEqualTo(user.getUserId());

        actual.setPassword(newPassword);
        assertThat(actual.getPassword()).isEqualTo(newPassword);
    }

    @Test
    @DisplayName("삭제 후 조회")
    void delete() {
        User user = new User(3L, "sykim", "password", "name", "sykim@sykim.com");
        User savedUser = users.save(user);
        Optional<User> actual = users.findById(savedUser.getId());
        assertThat(actual).isPresent();

        users.deleteById(actual.get().getId());
        actual = users.findById(actual.get().getId());
        assertThat(actual).isNotPresent();
    }
}
