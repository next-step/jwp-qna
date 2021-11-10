package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        final User savedUser = userRepository.save(TestDummy.USER_JAVAJIGI);

        assertAll(
            () -> assertThat(savedUser.getUserId()).isEqualTo(TestDummy.USER_JAVAJIGI.getUserId()),
            () -> assertThat(savedUser.getPassword()).isEqualTo(TestDummy.USER_JAVAJIGI.getPassword()),
            () -> assertThat(savedUser.getName()).isEqualTo(TestDummy.USER_JAVAJIGI.getName()),
            () -> assertThat(savedUser.getEmail()).isEqualTo(TestDummy.USER_JAVAJIGI.getEmail())
        );
    }

    @Test
    void read() {
        final Long savedId = userRepository.save(TestDummy.USER_JAVAJIGI).getId();
        final User savedUser = userRepository.findByUserId(TestDummy.USER_JAVAJIGI.getUserId()).get();

        assertAll(
            () -> assertThat(savedUser.getId()).isEqualTo(savedId),
            () -> assertThat(savedUser.getUserId()).isEqualTo(TestDummy.USER_JAVAJIGI.getUserId()),
            () -> assertThat(savedUser.getPassword()).isEqualTo(TestDummy.USER_JAVAJIGI.getPassword()),
            () -> assertThat(savedUser.getName()).isEqualTo(TestDummy.USER_JAVAJIGI.getName()),
            () -> assertThat(savedUser.getEmail()).isEqualTo(TestDummy.USER_JAVAJIGI.getEmail())
        );
    }
}
