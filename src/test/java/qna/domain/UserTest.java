package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest(includeFilters = { @ComponentScan.Filter(value = { EnableJpaAuditing.class }) })
public class UserTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUserId() {
        final User JAVAJIGI = new User(null, "javajigi", "password", "name", "javajigi@slipp.net");
        final User SANJIGI = new User(null, "sanjigi", "password", "name", "sanjigi@slipp.net");

        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);

        User actual = userRepository.findByUserId(JAVAJIGI.getUserId()).get();

        assertAll(
                () -> assertThat(actual.getUserId()).isEqualTo(JAVAJIGI.getUserId())
        );
    }
}
