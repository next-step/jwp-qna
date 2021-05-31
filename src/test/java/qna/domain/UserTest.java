package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void deleteAll() {
        userRepository.deleteAll();
    }

    @Test
    void save() {
        User expected = JAVAJIGI;
        User actual = userRepository.save(expected);
        assertAll(
                () -> Assertions.assertThat(actual.getId()).isNotNull(),
                () -> Assertions.assertThat(actual.getPassword().get()).isEqualTo(expected.getPassword().get()),
                () -> Assertions.assertThat(actual.getEmail().get()).isEqualTo(expected.getEmail().get()),
                () -> Assertions.assertThat(actual.getUserId().get()).isEqualTo(expected.getUserId().get()),
                () -> Assertions.assertThat(actual.getName().get()).isEqualTo(expected.getName().get())
        );
    }

    @Test
    void findById() {
        User expected = userRepository.save(JAVAJIGI);
        User actual1 = userRepository.findById(expected.getId()).get();
        assertAll(
                () -> Assertions.assertThat(actual1.getId()).isNotNull(),
                () -> Assertions.assertThat(actual1.getPassword()).isEqualTo(expected.getPassword()),
                () -> Assertions.assertThat(actual1.getEmail()).isEqualTo(expected.getEmail()),
                () -> Assertions.assertThat(actual1.getUserId()).isEqualTo(expected.getUserId()),
                () -> Assertions.assertThat(actual1.getName()).isEqualTo(expected.getName())
        );
    }

    @Test
    void findByUserId() {
        User expected = userRepository.save(JAVAJIGI);
        User actual2 = userRepository.findByUserId(expected.getUserId()).get();
        assertAll(
                () -> Assertions.assertThat(actual2.getId()).isNotNull(),
                () -> Assertions.assertThat(actual2.getPassword()).isEqualTo(expected.getPassword()),
                () -> Assertions.assertThat(actual2.getEmail()).isEqualTo(expected.getEmail()),
                () -> Assertions.assertThat(actual2.getUserId()).isEqualTo(expected.getUserId()),
                () -> Assertions.assertThat(actual2.getName()).isEqualTo(expected.getName())
        );
    }

    @Test
    void update() {
        User user = userRepository.save(JAVAJIGI);
        User expect = new User(user.getId(), user.getUserId().get(), user.getPassword().get(), "updateName", "updateEmail@uos.ac.kr");

        user.update(user, expect);

        assertThat(userRepository.findByUserId(user.getUserId()).get()).isEqualTo(expect);
    }

    @Test
    void delete() {
        User user = userRepository.save(JAVAJIGI);
        userRepository.deleteById(user.getId());

        assertThat(userRepository.findByUserId(user.getUserId())).isNotPresent();
    }

}
