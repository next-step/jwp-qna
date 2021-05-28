package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository users;

    @DisplayName("저장하기")
    @Test
    void save() {
        User user = new User("testUserId", "testPassword", "testName", "test@email.com");

        User saveUser = users.save(user);

        assertThat(saveUser).isEqualTo(user);
        assertThat(saveUser).isSameAs(user);
    }

    @DisplayName("수정하기")
    @Test
    void update() {
        User user = new User("testUserId", "testPassword", "testName", "test@email.com");
        User saveUser = users.save(user);

        saveUser.setName("testUpdateName");

        User findUser = users.findById(user.getId()).orElseThrow(() -> new IllegalStateException());
        assertThat(findUser.getName()).isEqualTo("testUpdateName");
    }

    @DisplayName("삭제하기")
    @Test
    void delete() {
        User user = new User("testUserId", "testPassword", "testName", "test@email.com");
        User saveUser = users.save(user);

        users.delete(saveUser);

        assertThat(users.findById(saveUser.getId())).isEqualTo(Optional.empty());
    }
}
