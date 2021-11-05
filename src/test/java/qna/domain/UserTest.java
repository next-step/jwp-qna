package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserTest {

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    private User savedJavajigi;
    private User savedSanjigi;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        savedJavajigi = userRepository.save(JAVAJIGI);
        savedSanjigi = userRepository.save(SANJIGI);
    }

    @DisplayName("회원을 저장한다.")
    @Test
    void save() {
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
        assertThat(userRepository.findByUserId("javajigi").get()).isEqualTo(savedJavajigi);
    }

    @DisplayName("회원의 정보를 수정한다.")
    @Test
    void update() {
        savedJavajigi.setEmail("javajigi@hanmail.com");

        assertThat(userRepository.findByUserId("javajigi").get().getEmail()).isEqualTo("javajigi@hanmail.com");
    }

}
