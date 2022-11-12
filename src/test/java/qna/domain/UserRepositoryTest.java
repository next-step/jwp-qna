package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository users;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    }

    @Test
    void find() {
        final User saved = users.save(user);
        final User actual = users.findById(saved.getId()).get();
        assertThat(actual).isEqualTo(saved);
        assertThat(actual).isSameAs(saved);
    }

    @Test
    void update() {
        final User saved = users.save(user);

        // Update name and email
        User updated = users.findById(saved.getId()).get();
        updated.update(updated, new User(saved.getUserId(), saved.getPassword(), "withbeth", "wbg"));

        final User actual = users.findById(saved.getId()).get();
        assertThat(actual.getName()).isEqualTo("withbeth");
        assertThat(actual.getEmail()).isEqualTo("wbg");
    }

    @Test
    void delete() {
        final User saved = users.save(user);

        users.deleteById(saved.getId());
        assertThat(users.findById(saved.getId()).orElse(null)).isNull();
    }

}
