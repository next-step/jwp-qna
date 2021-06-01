package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository users;

    @Test
    void save() {
        User expected = UserTest.JAVAJIGI;
        User actual = users.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getUserId()).isNotNull(),
                () -> assertThat(actual.getPassword()).isNotNull(),
                () -> assertThat(actual.getName()).isNotNull(),
                () -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId()),
                () -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
        );
    }

    @Test
    void findByName() {
        User expected = UserTest.JAVAJIGI;
        users.save(expected);
        User actual = users.findByUserId(expected.getUserId()).get();
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    void update() {
        User expected = UserTest.JAVAJIGI;
        User saved = users.save(expected);

        saved.setEmail("beck33333@naver.com");
        users.flush();
    }

    @Test
    void delete() {
        User expected = UserTest.JAVAJIGI;
        User saved = users.save(expected);

        users.delete(saved);
        users.flush();
    }
}