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
    public void save() {
        User actual = users.save(UserTest.JUNSEONG);
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    public void findById() {
        User actual = users.findByUserId(UserTest.JAVAJIGI.getUserId()).get();
        assertThat(actual.getUserId()).isEqualTo("javajigi");
    }
}