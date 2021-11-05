package qna.domain;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserTest {

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @DisplayName("회원을 저장한다.")
    @Test
    void save() {
        User savedJavajigi = userRepository.save(JAVAJIGI);
        User savedSanjigi = userRepository.save(SANJIGI);

        assertAll(
                () -> assertThat(savedJavajigi.getId()).isNotNull(),
                () -> assertThat(savedJavajigi.getName()).isEqualTo(JAVAJIGI.getName()),
                () -> assertThat(savedSanjigi.getId()).isNotNull(),
                () -> assertThat(savedSanjigi.getName()).isEqualTo(SANJIGI.getName())
        );
    }

    @DisplayName("아이디로 회원을 조회한다.")
    @Test
    void findByUserId() {
        User savedJavajigi = userRepository.save(JAVAJIGI);

        assertThat(userRepository.findByUserId(JAVAJIGI.getUserId()).get()).isEqualTo(savedJavajigi);
    }

    @DisplayName("회원의 정보를 수정한다.")
    @Test
    void update() {
        User savedJavajigi = userRepository.save(JAVAJIGI);
        savedJavajigi.setEmail("javajigi@hanmail.com");

        assertThat(userRepository.findByUserId(JAVAJIGI.getUserId()).get().getEmail()).isEqualTo("javajigi@hanmail.com");
    }

}
