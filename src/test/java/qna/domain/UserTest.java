package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.UnAuthorizedException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    private User saveJAVAJIGI;

    @BeforeAll
    void setUp() {
        saveJAVAJIGI = userRepository.save(JAVAJIGI);
    }

    @Test
    @DisplayName("User save 테스트 ")
    @Order(1)
    void save() {
        assertThat(saveJAVAJIGI).isNotNull();
    }

    @Test
    @DisplayName("이름과 이메일이 같은지 테스트")
    @Order(2)
    void equalsNameAndEmail() {
        User loginUser = new User("javajigi", "password", "name", "javajigi@slipp.net");
        assertThat(saveJAVAJIGI.equalsNameAndEmail(null)).isFalse();
        assertThat(saveJAVAJIGI.equalsNameAndEmail(SANJIGI)).isFalse();
        assertThat(saveJAVAJIGI.equalsNameAndEmail(loginUser)).isTrue();
    }

    @Test
    @DisplayName("User update 메소드 유효성 검사")
    void update_실패() {
        User loginUser = new User("test", "password", "name", "email");
        User target = new User("javajigi", "test", "name", "email");
        assertThatExceptionOfType(UnAuthorizedException.class)
            .isThrownBy(() -> saveJAVAJIGI.update(loginUser, target));
    }

    @Test
    @DisplayName("User update 성공")
    void update_성공() {
        User loginUser = new User("javajigi", "password", "name", "email");
        User target = new User("javajigi", "password", "name", "email");
        saveJAVAJIGI.update(loginUser, target);
        userRepository.flush();
        assertThat(target.getEmail()).isEqualTo(saveJAVAJIGI.getEmail());
        assertThat(target.getName()).isEqualTo(saveJAVAJIGI.getName());
    }

}
