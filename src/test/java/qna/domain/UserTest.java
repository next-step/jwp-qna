package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
public class UserTest {

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    UserRepository userRepository;

    @Test
    void exception_user_id_null() {
        // given
        User user = new User(null, "password", "name", "email");

        // when // then
        assertThatThrownBy(() -> {
            userRepository.save(user);
        }).isInstanceOf(DataIntegrityViolationException.class)
            .hasMessageContaining("null");
    }

    @Test
    void input_user_email_over_length_range() {
        // given
        String email = "12345678910123456789qweqw4e56qw4e56wq45e4wq56e45wq64e65wq45e6wq121233";
        User user = new User(3L, "test_user_id", "test_password", "test_name",
            email);

        // when // then
        assertThatThrownBy(() -> {
            userRepository.save(user);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void input_user_id_already_save() {
        //given
        User user_1 = new User("test_user_id", "test_password", "test_name", "test@test.com");
        User user_2 = new User("test_user_id", "test_password", "test_name", "test@test.com");

        userRepository.save(user_1);

        //when, then
        assertThatThrownBy(() -> {
            userRepository.save(user_2);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void read_user() {
        // given
        User user = new User("id", "test", "password", "name");
        User expectUser = userRepository.save(user);

        // when
        Optional<User> userOptional = userRepository.findById(expectUser.getId());

        // then
        assertThat(userOptional.isPresent()).isTrue();
    }
}
