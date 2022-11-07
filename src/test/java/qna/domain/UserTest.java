package qna.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    UserRepository users;


    @Test
    void 중복되는_유저아이디_save() {
        users.save(JAVAJIGI);
        User duplicationUser = new User(2L, "javajigi", "password", "name", "hello@its.me");
        assertThatThrownBy(() -> users.save(duplicationUser))
            .isInstanceOf(RuntimeException.class);
    }

    @ParameterizedTest(name = "save_테스트")
    @MethodSource("saveTestFixture")
    void save_테스트(User user) {
        User saved = users.save(user);
        assertThat(saved).isEqualTo(user);
    }

    @ParameterizedTest(name = "save_후_findById_테스트")
    @MethodSource("saveTestFixture")
    void save_후_findById_테스트(User user) {
        User user1 = users.save(user);
        User user2 = users.findById(user1.getId()).get();
        assertThat(user1).isEqualTo(user2);
    }

    static Stream<User> saveTestFixture() {
        return Stream.of(JAVAJIGI, SANJIGI);
    }
}
