package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    void setUp() {
        user = new User("javajigi", "password", "name", "javajigi@slipp.net");
        userRepository.save(user);
    }

    @Test
    void 생성() {
        assertThat(user.getId()).isNotNull();
    }

    @Test
    void 수정() {
        user.setName("name1");
        User findUser = userRepository.findById(user.getId()).get();
        assertThat(findUser.getName()).isEqualTo("name1");
    }

    @Test
    void 조회() {
        User findUser = userRepository.findById(user.getId()).get();
        assertThat(findUser).isSameAs(user);
    }

    @Test
    void 삭제() {
        userRepository.delete(user);
        assertThat(userRepository.findById(user.getId())).isNotPresent();
    }

    @Test
    void 아이디_NotNull() {
        User user = new User(null, "password", "name", "javajigi@slipp.net");
        assertThatThrownBy(() -> userRepository.save(user))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void 아이디_최대길이_20() {
        String size21 = new String(new char[21]).replace("\0", "a");
        User user = new User(size21, "password", "name", "javajigi@slipp.net");
        assertThatThrownBy(() -> userRepository.save(user))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void 아이디_중복불가() {
        User newUser = new User(user.getUserId(), "password", "name", "javajigi@slipp.net");
        assertThatThrownBy(() -> userRepository.save(newUser))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
