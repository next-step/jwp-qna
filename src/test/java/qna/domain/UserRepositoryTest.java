package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository users;

    private User u1;

    @BeforeEach
    void setUp() {
        u1 = new User("seungyeol", "password", "name", "beck33333@naver.com");
    }
    @AfterEach
    void deleteAll() {
        users.deleteAll();
    }

    @Test
    void save() {
        User expected = u1;
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
        User expected = u1;
        users.save(expected);
        User actual = users.findByUserId(expected.getUserId()).get();
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    void update() {
        User expected = u1;
        User saved = users.save(expected);

        saved.setEmail("beck33333@naver.com");
        users.flush();
    }

    @Test
    void delete() {
        User expected = u1;
        User saved = users.save(expected);

        users.delete(saved);
        users.flush();
    }
}