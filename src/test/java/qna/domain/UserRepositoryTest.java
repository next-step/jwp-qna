package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository users;

    @Test
    public void save() {
        User expected = new User("chajs", "1234", "차준성", "chajs226@gmail.com");
        User actual = users.save(expected);

        assertThat(actual.getId()).isNotNull();
    }

    @Test
    public void findById() {
        User expected = new User("chajs", "1234", "차준성", "chajs226@gmail.com");
        User actual = users.save(expected);

        assertThat(actual.getUserId()).isEqualTo("chajs");
    }
}