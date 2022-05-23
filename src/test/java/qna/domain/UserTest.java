package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import qna.NotFoundException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest(includeFilters = { @ComponentScan.Filter(value = { EnableJpaAuditing.class }) })
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @Test
    void userId_가_다르면_다른_User_이다() {
        save_users();

        User U1 = userRepository.findByUserId(JAVAJIGI.getUserId()).orElseThrow(NotFoundException::new);
        User U2 = userRepository.findByUserId(SANJIGI.getUserId()).orElseThrow(NotFoundException::new);

        assertAll(
                () -> assertThat(U1.equals(U2)).isFalse(),
                () -> assertThat(U1.getUserId()).isNotEqualTo(U2.getUserId())
        );
    }

    private void save_users() {
        assertDoesNotThrow(() -> userRepository.save(JAVAJIGI));
        assertDoesNotThrow(() -> userRepository.save(SANJIGI));
    }
}
