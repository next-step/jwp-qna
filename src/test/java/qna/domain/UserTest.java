package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserTest {

    public static final User JAVAJIGI = User.of("javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = User.of("sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @DisplayName("save 테스트")
    @Test
    void save() {
        // when
        User newUser = userRepository.save(JAVAJIGI);

        // then
        assertAll(
                () -> assertThat(newUser.getId()).isNotNull()
                , () -> assertThat(newUser.getUserId()).isEqualTo(JAVAJIGI.getUserId())
                , () -> assertThat(newUser.getPassword()).isEqualTo(JAVAJIGI.getPassword())
                , () -> assertThat(newUser.getName()).isEqualTo(JAVAJIGI.getName())
                , () -> assertThat(newUser.getEmail()).isEqualTo(JAVAJIGI.getEmail())
        );
    }

    @DisplayName("findByUserId 테스트")
    @Test
    void findByUserId() {
        // given
        User newUser = userRepository.save(JAVAJIGI);

        // when
        User findUser = userRepository.findByUserId(newUser.getUserId()).get();

        // then
        assertAll(
                () -> assertThat(findUser.getId()).isNotNull()
                , () -> assertThat(findUser).isEqualTo(newUser)
        );
    }
}
