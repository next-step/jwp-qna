package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        final User savedUser = userRepository.save(JAVAJIGI);

        assertAll(
            () -> assertThat(savedUser.getUserId()).isEqualTo(JAVAJIGI.getUserId()),
            () -> assertThat(savedUser.getPassword()).isEqualTo(JAVAJIGI.getPassword()),
            () -> assertThat(savedUser.getName()).isEqualTo(JAVAJIGI.getName()),
            () -> assertThat(savedUser.getEmail()).isEqualTo(JAVAJIGI.getEmail())
        );
    }

    @Test
    void read() {
        final Long savedId = userRepository.save(JAVAJIGI).getId();
        final User savedUser = userRepository.findByUserId(JAVAJIGI.getUserId()).get();

        assertAll(
            () -> assertThat(savedUser.getId()).isEqualTo(savedId),
            () -> assertThat(savedUser.getUserId()).isEqualTo(JAVAJIGI.getUserId()),
            () -> assertThat(savedUser.getPassword()).isEqualTo(JAVAJIGI.getPassword()),
            () -> assertThat(savedUser.getName()).isEqualTo(JAVAJIGI.getName()),
            () -> assertThat(savedUser.getEmail()).isEqualTo(JAVAJIGI.getEmail())
        );
    }
}
