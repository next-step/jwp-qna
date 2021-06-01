package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository users;

    @BeforeEach
    void setUp() {
        users.save(UserTest.JAVAJIGI);
    }

    @Test
    public void save_테스트() {
        User user_saved = users.save(UserTest.JUNSEONG);
        assertThat(user_saved.getId()).isNotNull();
    }

    @Test
    public void findById_테스트() {
        User actual = users.findByUserId(UserTest.JAVAJIGI.getUserId()).get();
        assertThat(actual.getUserId()).isEqualTo("javajigi");
    }

    @Test
    public void matchPassword_테스트() {
        User actual = users.findByUserId(UserTest.JAVAJIGI.getUserId()).get();
        assertThat(actual.matchPassword("password")).isTrue();
    }
}