package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import qna.UnAuthorizedException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("사용자를 저장할 수 있어야 한다.")
    void save_user() {
        User user = userRepository.save(JAVAJIGI);

        assertAll(
                () -> assertThat(user.getId()).isNotNull(),
                () -> assertThat(user.getUserId()).isEqualTo(JAVAJIGI.getUserId()),
                () -> assertThat(user.getPassword()).isEqualTo(JAVAJIGI.getPassword()),
                () -> assertThat(user.getName()).isEqualTo(JAVAJIGI.getName()),
                () -> assertThat(user.getEmail()).isEqualTo(JAVAJIGI.getEmail()),
                () -> assertThat(user.getCreatedAt()).isNotNull()
        );
    }

    @Test
    @DisplayName("userId로 User를 조회할 수 있어야 한다.")
    void get_user_by_id() {
        List<User> users = userRepository.saveAll(Arrays.asList(JAVAJIGI, SANJIGI));

        User savedUser = userRepository.findByUserId(users.get(0).getUserId()).get();

        assertThat(users.get(0)).isEqualTo(savedUser);
    }

    @Test
    @DisplayName("User를 삭제한다")
    void delete_user() {
        List<User> users = userRepository.saveAll(Arrays.asList(JAVAJIGI, SANJIGI));
        userRepository.delete(users.get(0));

        List<User> savedUsers = userRepository.findAll();

        assertThat(savedUsers.size()).isEqualTo(users.size()-1);
    }

    @Test
    @DisplayName("User 정보를 업데이트 한다")
    void update_user_by_id() {
        User user = userRepository.save(JAVAJIGI);

        JAVAJIGI.updateEmail("heollo@test.com");
        user.update(user, JAVAJIGI);

        User updatedUser = userRepository.findByUserId(user.getUserId()).get();


        assertAll(
                () -> assertThat(updatedUser).isEqualTo(user),
                () -> assertThat(updatedUser.equalsNameAndEmail(JAVAJIGI)).isEqualTo(true)
        );
    }

    @Test
    @DisplayName("UserId는 유니크해야 한다.")
    void unique_user_id() {
        userRepository.saveAll(Arrays.asList(JAVAJIGI, SANJIGI));

        assertThatThrownBy(() ->
                userRepository.save(new User(3L, "javajigi", "password3", "name3", "javajigi3@slipp.net"))
        ).isInstanceOf(DataIntegrityViolationException.class);
    }
}
