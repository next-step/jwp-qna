package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Test
    @DisplayName("User 생성 시 id가 null 이 아닌지, 저장 전/후의 데이터 값이 같은지 확인")
    void create() {
        User user = UserTest.JAVAJIGI;
        User savedUser = userRepository.save(user);
        Assertions.assertThat(savedUser.getId()).isNotNull();
    }

    @Transactional
    @Test
    @DisplayName("이미 존재하는 userId 로 User 생성 시 예외를 발생시킨다. (unique = true 테스트)")
    void create2() {
        User savedUser = userRepository.save(UserTest.JAVAJIGI);
        User duplicatedUser = new User(null, "javajigi", "password", "user", "user@gmail.com");

        Assertions.assertThatThrownBy(() -> userRepository.save(duplicatedUser))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Transactional
    @Test
    @DisplayName("userId 가 null 이면 예외를 발생시킨다. (nullable = false 테스트)")
    void create3() {
        User user = new User(null, null, "password", "user", "user@gmail.com");

        Assertions.assertThatThrownBy(() -> userRepository.save(user))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Transactional
    @Test
    @DisplayName("userId 가 20자 초과하면 예외를 발생시킨다. (length = 20 테스트)")
    void create4() {
        User user = new User(null, "123456789012345678901", "password", "user", "user@gmail.com");

        Assertions.assertThatThrownBy(() -> userRepository.save(user))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Transactional
    @Test
    @DisplayName("userId로 User 조회 시 조회되는지 확인")
    void read() {
        userRepository.save(UserTest.JAVAJIGI);

        Optional<User> findUser = userRepository.findByUserId("javajigi");

        Assertions.assertThat(findUser).isPresent();
    }

    @Transactional
    @Test
    @DisplayName("저장한 User 삭제 후 조회 시 User 가 없는지 확인")
    void delete() {
        User saveUser = userRepository.save(UserTest.JAVAJIGI);
        userRepository.delete(saveUser);

        Optional<User> findUser = userRepository.findById(saveUser.getId());

        assertThat(findUser).isNotPresent();
    }
}
