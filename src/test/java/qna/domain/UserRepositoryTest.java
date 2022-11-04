package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
@DisplayName("사용자 테스트")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("사용자 저장 확인")
    void save() {
        User user = userRepository.save(JAVAJIGI);
        User savedUser = userRepository.save(user);

        assertAll(
                () -> assertThat(savedUser).isNotNull(),
                () -> assertThat(savedUser.getId()).isNotNull(),
                () -> assertThat(savedUser.getId()).isEqualTo(user.getId()),
                () -> assertThat(savedUser.getUserId()).isEqualTo(user.getUserId()),
                () -> assertThat(savedUser.getPassword()).isEqualTo(user.getPassword()),
                () -> assertThat(savedUser.getName()).isEqualTo(user.getName()),
                () -> assertThat(savedUser.getEmail()).isEqualTo(user.getEmail())
        );
    }

    @Test
    @DisplayName("사용자 저장 아이디 예외 확인")
    void save_user_id_exception() {
        User savedUser = userRepository.save(JAVAJIGI);
        User duplicatedUser = new User(null, savedUser.getUserId(), "password", "name", "email");

        assertThatThrownBy(() -> userRepository.save(duplicatedUser))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("저장 정보 길이 테스트")
    @ParameterizedTest(name = "엔티티에서 정의한 길이에서는 사용자를 생성할 수 없다.")
    @MethodSource("users_info")
    void save_user_length_check(User user) {
        assertThatThrownBy(() -> userRepository.save(user))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    private static Stream<Arguments> users_info() {
        return Stream.of(
                Arguments.of(new User(null, "1111_2222_3333_4444_5", "password", "name", "email")),
                Arguments.of(new User(null, "userId", "1111_2222_3333_4444_5", "name", "email")),
                Arguments.of(new User(null, "userId", "password", "1111_2222_3333_4444_5", "email")),
                Arguments.of(new User(null, "userId", "password", "name", "1111_2222_3333_4444_5555_6666_7777_8888_9999_0000_1"))
        );
    }

    @Test
    @DisplayName("사용자 조회 확인")
    void read() {
        User savedUser = userRepository.save(JAVAJIGI);

        Optional<User> findUser = userRepository.findByUserId(savedUser.getUserId());

        assertThat(savedUser).isEqualTo(findUser.get());
        assertThat(findUser).isPresent();
    }

    @Test
    @DisplayName("사용자 삭제 확인")
    void delete() {
        User saveUser = userRepository.save(JAVAJIGI);
        userRepository.delete(saveUser);

        Optional<User> findUser = userRepository.findById(saveUser.getId());

        assertThat(findUser).isEmpty();
        assertThat(findUser).isNotPresent();
    }

}
