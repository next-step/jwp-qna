package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @DisplayName("User 을 저장할 수 있다.")
    @Test
    void save() {
        User actual = userRepository.save(UserTest.JAVAJIGI);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getEmail()).isEqualTo(UserTest.JAVAJIGI.getEmail()),
                () -> assertThat(actual.getName()).isEqualTo(UserTest.JAVAJIGI.getName()),
                () -> assertThat(actual.getPassword()).isEqualTo(UserTest.JAVAJIGI.getPassword()),
                () -> assertThat(actual.getUserId()).isEqualTo(UserTest.JAVAJIGI.getUserId()));
    }

    @DisplayName("저장된 User을 조회할 수 있다.")
    @Test
    void find() {
        User actual = userRepository.save(UserTest.JAVAJIGI);

        Optional<User> expect = userRepository.findById(actual.getId());

        assertThat(expect).isPresent();
    }

    @DisplayName("저장된 User을 삭제할 수 있다.")
    @Test
    void delete() {
        User actual = userRepository.save(UserTest.JAVAJIGI);

        userRepository.delete(actual);
        Optional<User> expect = userRepository.findById(actual.getId());

        assertThat(expect).isNotPresent();
    }

    @DisplayName("저장된 User을 값을 변경할 수 있다.")
    @Test
    void update() {
        User actual = userRepository.save(new User("userId", "password", "name", "id@kr.com"));
        String expectName = "lee";

        actual.setName(expectName);
        Optional<User> expect = userRepository.findById(actual.getId());

        assertThat(expect.orElseThrow(NotFoundException::new).getName()).isEqualTo(expectName);
    }

    @DisplayName("UserId로 User을 조회할 수 있다.")
    @Test
    void find_by_user_id() {
        String userId = "userId";
        User actual = userRepository.save(new User(userId, "password", "name", "id@kr.com"));

        Optional<User> expect = userRepository.findByUserId(userId);

        assertThat(expect.orElseThrow(NotFoundException::new)).isEqualTo(actual);
    }
}
