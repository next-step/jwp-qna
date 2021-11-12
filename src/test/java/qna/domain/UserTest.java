package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    UserRepository userRepository;

    @Test
    void save() {
        //when
        final User javajigi = userRepository.save(JAVAJIGI);
        final User sanjigi = userRepository.save(SANJIGI);

        //then
        assertAll(
            () -> assertThat(javajigi.equalsNameAndEmail(JAVAJIGI)).isTrue(),
            () -> assertThat(javajigi.matchPassword(JAVAJIGI.getPassword())).isTrue(),
            () -> assertThat(javajigi.isGuestUser()).isFalse(),
            () -> assertThat(sanjigi.equalsNameAndEmail(SANJIGI)).isTrue(),
            () -> assertThat(sanjigi.matchPassword(SANJIGI.getPassword())).isTrue(),
            () -> assertThat(sanjigi.isGuestUser()).isFalse()
        );
    }

    @Test
    void findByUserId() {
        //given
        final User javajigi = userRepository.save(JAVAJIGI);

        //when
        final User actual = this.userRepository.findByUserId(javajigi.getUserId()).orElseThrow(RuntimeException::new);

        //then
        assertThat(actual).isNotNull().isEqualTo(javajigi);
    }
}
