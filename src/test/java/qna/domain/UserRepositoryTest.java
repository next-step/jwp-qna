package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository users;

    @Test
    void save() {
        User expected = new User("id", "password", "name", "email");
        User actual = users.save(expected);
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    void findById() {
        users.save(new User("id", "password", "name", "email"));
        Optional<User> actual = users.findById(0L);
        assertThat(actual).isNotNull();
    }
}
