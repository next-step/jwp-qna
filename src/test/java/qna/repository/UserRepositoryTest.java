package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.domain.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    private void saveUser() {
        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
    }

    @Test
    @DisplayName("데이터를 저장한다.")
    void save_test() {
        User save = userRepository.save(JAVAJIGI);
        assertAll(
                () -> assertThat(save.getId()).isNotNull(),
                () -> assertThat(save.getName()).isEqualTo(JAVAJIGI.getName())
        );
    }

    @Test
    @DisplayName("데이터를 모두 조회한다.")
    void find_all_test() {
        saveUser();
        List<User> users = userRepository.findAll();
        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Id로 데이터를 조회한다.")
    void find_by_id_test() {
        saveUser();
        Optional<User> userOptional = userRepository.findById(JAVAJIGI.getId());
        userOptional.ifPresent(user -> {
            assertAll(
                    () -> assertThat(user.getUserId()).isEqualTo("javajigi"),
                    () -> assertThat(user.getPassword()).isEqualTo("password"),
                    () -> assertThat(user.getName()).isEqualTo("name"),
                    () -> assertThat(user.getEmail()).isEqualTo("javajigi@slipp.net")
            );
        });
    }

    @Test
    @DisplayName("userId로 데이터를 조회한다.")
    void find_by_user_id_test() {
        saveUser();
        Optional<User> userOptional = userRepository.findByUserId(JAVAJIGI.getUserId());
        userOptional.ifPresent(user -> {
            assertAll(
                    () -> assertThat(user.getPassword()).isEqualTo("password"),
                    () -> assertThat(user.getName()).isEqualTo("name"),
                    () -> assertThat(user.getEmail()).isEqualTo("javajigi@slipp.net")
            );
        });
    }

    @Test
    @DisplayName("데이터를 삭제한다.")
    void delete_test() {
        saveUser();
        Optional<User> userOptional = userRepository.findByUserId(JAVAJIGI.getUserId());
        userOptional.ifPresent(user -> {
            userRepository.delete(user);
        });

        List<User> users = userRepository.findAll();
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("전체 데이터를 삭제한다.")
    void delete_all_test() {
        saveUser();
        userRepository.deleteAll();
        List<User> users = userRepository.findAll();
        assertThat(users.size()).isZero();
    }

    @Test
    @DisplayName("데이터를 수정한다.")
    void update_test() {
        saveUser();
        User target = new User(1L, "javajigi", "password", "updated", "updated");
        Optional<User> userOptional = userRepository.findByUserId("javajigi");
        userOptional.ifPresent(user -> {
            user.update(user, target);
            userRepository.save(user);
        });

        userOptional = userRepository.findByUserId("javajigi");
        userOptional.ifPresent(user -> {
            assertThat(user.getName()).isEqualTo("updated");
        });
    }
}
