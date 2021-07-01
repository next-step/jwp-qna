package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static org.assertj.core.api.Assertions.assertThat;

@EnableJpaAuditing
@DataJpaTest
public class UserTest {

    @Autowired
    UserRepository users;

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    void save() {
        User actual = users.save(JAVAJIGI);
        assertThat(actual.getUserId()).isEqualTo("javajigi");
        assertThat(actual.getName()).isEqualTo("name");
    }
}
