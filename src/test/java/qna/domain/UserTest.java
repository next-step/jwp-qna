package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import qna.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest(includeFilters = {@ComponentScan.Filter(value = {EnableJpaAuditing.class})})
public class UserTest {
    @Autowired
    private UserRepository userRepository;

    private User JAVAJIGI;
    private User SANJIGI;

    @BeforeEach
    void setUp() {
        JAVAJIGI = new User(null, "javajigi", "password", "name", "javajigi@slipp.net");
        SANJIGI = new User(null, "sanjigi", "password", "name", "sanjigi@slipp.net");

        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
    }

    @Test
    void findByUserId() {
        //when
        User actual = userRepository.findByUserId(JAVAJIGI.getUserId()).orElseThrow(NotFoundException::new);

        //then
        assertAll(
                () -> assertThat(actual.getUserId()).isEqualTo(JAVAJIGI.getUserId())
        );
    }
}
