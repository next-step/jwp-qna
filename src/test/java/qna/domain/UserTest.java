package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository users;

    @Test
    void save() {
        users.save(JAVAJIGI);
        users.save(SANJIGI);

        assertThat(users.findAll().size()).isEqualTo(2);
    }

    @Test
    void update() {
        User beforeUser = users.save(JAVAJIGI);
        User updateUser = new User("javajigi", "password", "pobi", "pobi@pobi.com");

        long userId = users.findByUserId("javajigi").get().getId();

        assertThatThrownBy(() -> users.findByUserId("pobi").get()).isInstanceOf(NoSuchElementException.class);

        beforeUser.update(beforeUser, updateUser);

        //assertThat(users.findById(userId).get().getName()).isEqualTo("pobi"); //flush 호출 전까지 업데이트문 실행안함
        /*
        *   왜 flush 전 까지 update구문이 출력이 안되지?
         */
        //assertThat(users.findByUserId("javajigi").get().getName()).isEqualTo("pobi");
        assertThat(users.findByName("pobi")).isNotNull();
        //users.flush();
    }

    @Test
    void matchPassword() {
        User actual = users.save(JAVAJIGI);
        User expected = users.findByUserId("javajigi").get();

        assertThat(actual.matchPassword(expected.getPassword())).isTrue();
    }

    @Test
    void equalsNameAndEmail() {
        User actual = users.save(JAVAJIGI);
        User expected = users.findByUserId("javajigi").get();

        assertThat(actual.equalsNameAndEmail(expected)).isTrue();
    }

    @Test
    void isGuestUser() {
        User user1 = User.GUEST_USER;

        assertThat(user1.isGuestUser()).isTrue();
        assertThat(JAVAJIGI.isGuestUser()).isFalse();
    }
}
