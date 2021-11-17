package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    UserRepository users;

    @Test
    void save() {
        final User user = users.save(JAVAJIGI);
        assertThat(user.getId()).isNotNull();
        assertThat(user.getUserId()).isEqualTo(JAVAJIGI.getUserId());
    }

    @Test
    void findByUserId() {
        users.save(JAVAJIGI);
        users.save(SANJIGI);
        final User user = users.findByUserId(SANJIGI.getUserId()).get();
        assertThat(user.getUserId()).isEqualTo(SANJIGI.getUserId());
        assertThat(user.getEmail()).isEqualTo(SANJIGI.getEmail());
    }
}
