package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("save하면 id가 자동으로 생성되야 한다.")
    void saveTest() {
        User save = userRepository.save(UserTest.JAVAJIGI);
        assertAll(
                () -> assertThat(save.getId()).isNotNull(),
                () -> assertThat(save.getName()).isEqualTo(UserTest.JAVAJIGI.getName())
        );
    }

    @Test
    @DisplayName("findById 결과는 동일성이 보장되어야한다.")
    void findByIdTest() {
        User save = userRepository.save(UserTest.JAVAJIGI);

        User find = userRepository.findById(save.getId()).get();

        assertThat(find).isEqualTo(save);
    }

    @Test
    @DisplayName("nullable false 칼럼은 반드시 값이 있어야한다.")
    void nullableTest() {
        User user = new User("ssw", null, null, null);

        assertThatThrownBy(() -> userRepository.save(user))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("이미 존재하는 userId를 save 할때 예외가 발생한다.")
    void uniqueColumnTest() {
        userRepository.save(UserTest.JAVAJIGI);

        assertThatThrownBy(() -> userRepository.save(new User(UserTest.JAVAJIGI.getUserId(), "1234", "sungwoo", "ssw@kakao.com")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("update를 하면 이름과 이메일이 수정된다.")
    void updateTest() {
        User savedUser = userRepository.save(UserTest.JAVAJIGI);
        User updateUser = new User(savedUser.getUserId(), savedUser.getPassword(), "sungwoo", "ssw0418@kakao.com");
        savedUser.update(savedUser, updateUser);

        User findUser = userRepository.findByUserId(savedUser.getUserId()).get();

        assertAll(
                () -> assertThat(findUser.getName()).isNotEqualTo(UserTest.JAVAJIGI.getName()),
                () -> assertThat(findUser.getEmail()).isNotEqualTo(UserTest.JAVAJIGI.getEmail()),
                () -> assertThat(findUser.getName()).isEqualTo("sungwoo"),
                () -> assertThat(findUser.getEmail()).isEqualTo("ssw0418@kakao.com")
        );
    }
}