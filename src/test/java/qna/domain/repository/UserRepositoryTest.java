package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        User expected = new User("Liv", "1234", "ksh", "step@step.com");
        User actual = userRepository.save(expected);
        assertThat(expected).isEqualTo(actual);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }

    @Test
    void findByUserId() {
        User user = userRepository.findByUserId("ul8415").get();
        assertAll(
                () -> assertThat(user.getId()).isNotNull(),
                () -> assertThat(user.getName()).isEqualTo("홍길동"),
                () -> assertThat(user.getUserId()).isEqualTo("ul8415")
        );
    }
}
