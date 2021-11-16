package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository users;

    @Test
    @DisplayName("Create User - 저장된 ID, email 확인")
    void saveUser() {
        User savedUser = users.save(JAVAJIGI);

        assertAll(
            () -> assertThat(savedUser.getId()).isNotNull(),
            () -> assertThat(savedUser.getEmail()).isEqualTo(JAVAJIGI.getEmail())
        );
    }

    @Test
    @DisplayName("Read User - userId 로 조회한 User 의 Id, Name 을 확인")
    void findByUserId() {
        User savedUser = users.save(JAVAJIGI);
        Optional<User> foundUser = users.findByUserId(JAVAJIGI.getUserId());

        assertAll(
            () -> assertThat(foundUser).isPresent(),
            () -> assertThat(foundUser.get().getId()).isNotNull(),
            () -> assertThat(foundUser.get().getName()).isEqualTo(savedUser.getName())
        );
    }

    @Test
    @DisplayName("Update User - userId 변경 후 변경된 내용 확인")
    void updateUser() {
        User savedUser = users.save(JAVAJIGI);
        User foundUser = users.findByUserId(JAVAJIGI.getUserId()).get();
        foundUser.setUserId(SANJIGI.getUserId());

        Optional<User> foundUserByUserId = users.findByUserId(SANJIGI.getUserId());

        assertAll(
            () -> assertThat(foundUserByUserId).isPresent(),
            () -> assertThat(foundUser).isSameAs(foundUserByUserId.get())
        );
    }

    @Test
    @DisplayName("Delete User - 삭제 후 존재하지 않는 것을 확인")
    void deleteUser() {
        User savedUser = users.save(JAVAJIGI);
        User foundUser = users.findByUserId(JAVAJIGI.getUserId()).get();

        users.delete(foundUser);

        Optional<User> foundUserByUserId = users.findByUserId(foundUser.getUserId());

        assertAll(
            () -> assertThat(foundUserByUserId).isNotPresent()
        );
    }
}
