package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import javax.jws.soap.SOAPBinding.Use;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.Question;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserTest.JAVAJIGI);
    }

    @Test
    @DisplayName("userId 기준으로 User 출력 확인")
    void User_findByUserId() {
        em.clear();
        Optional<User> optionalUser = userRepository.findByUserId(UserTest.JAVAJIGI.getUserId());
        assertThat(optionalUser.isPresent()).isTrue();
    }

    @Test
    @DisplayName("findById가 정상적으로 이루어지는지 확인")
    void User_select() {
        em.clear();
        Optional<User> user = userRepository.findById(this.user.getId());
        assertThat(user.isPresent()).isTrue();
    }

    @Test
    @DisplayName("save가 정상적으로 이루어지는지 확인")
    void User_save() {
        assertThat(user.getId()).isNotNull();
    }

    @Test
    @DisplayName("update가 정상적으로 이루어지는지 확인")
    void Question_update() {
        user.setEmail("update email");
        userRepository.flush();
        Optional<User> user = userRepository.findById(this.user.getId());
        assertThat(user.get().getEmail()).isEqualTo("update email");
    }

    @Test
    @DisplayName("delete가 정상적으로 이루어지는지 확인")
    void User_delete() {
        userRepository.delete(user);
        userRepository.flush();
        Optional<User> user = userRepository.findById(this.user.getId());
        assertThat(user.isPresent()).isFalse();
    }

}
