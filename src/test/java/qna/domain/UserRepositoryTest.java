package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        final User expected = TestUserFactory.create(
            "javajigi", "password", "name", "javajigi@slipp.net"
        );
        final User actual = userRepository.save(expected);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getUserId()).isNotNull(),
            () -> assertThat(actual.getPassword()).isNotNull(),
            () -> assertThat(actual.getName()).isNotNull(),
            () -> assertThat(actual.getEmail()).isNotNull(),
            () -> assertThat(actual).isEqualTo(expected)
        );
    }

    @Test
    void findByUserId() {
        final User expected = TestUserFactory.create(
            "javajigi", "password", "name", "javajigi@slipp.net"
        );
        userRepository.save(expected);
        final User actual = userRepository.findByUserId(expected.getUserId())
            .orElseThrow(NoSuchElementException::new);
        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId())
        );
    }
}
