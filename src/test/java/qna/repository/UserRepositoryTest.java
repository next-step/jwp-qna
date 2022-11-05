package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;
import qna.domain.UserTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("User 저장 테스트")
    void saveUser() {
        User user = UserTest.JAVAJIGI;
        User saveUser = userRepository.save(user);

        assertAll(
                () -> assertThat(saveUser.getId()).isNotNull(),
                () -> assertThat(saveUser.getUserId()).isEqualTo(user.getUserId()),
                () -> assertThat(saveUser.getPassword()).isEqualTo(user.getPassword()),
                () -> assertThat(saveUser.getName()).isEqualTo(user.getName()),
                () -> assertThat(saveUser.getEmail()).isEqualTo(user.getEmail())
        );
    }

    @Test
    @DisplayName("User 2건 저장 후 전체 조회 테스트")
    void saveAllUser() {
        User user1 = UserTest.JAVAJIGI;
        User user2 = UserTest.SANJIGI;
        userRepository.saveAll(Arrays.asList(user1, user2));

        List<User> Users = userRepository.findAll();

        assertThat(Users).hasSize(2);
    }

    @Test
    @DisplayName("User 저장 후 User 조회 테스트")
    void readUser() {
        User saveUser = userRepository.save(UserTest.JAVAJIGI);
        Optional<User> findUser = userRepository.findById(saveUser.getId());

        assertThat(findUser).isPresent();
        assertThat(findUser.get()).isSameAs(saveUser);
    }

    @Test
    @DisplayName("User 저장 후 User name 수정 테스트")
    void updateUser() {
        User saveUser = userRepository.save(UserTest.JAVAJIGI);
        saveUser.setName(UserTest.SANJIGI.getName());

        Optional<User> findUser = userRepository.findById(saveUser.getId());

        assertThat(findUser).isPresent();
        assertThat(saveUser.getName()).isEqualTo(findUser.get().getName());
    }

    @Test
    @DisplayName("User 저장 후 User 삭제 테스트")
    void deleteUser() {
        User saveUser = userRepository.save(UserTest.JAVAJIGI);
        userRepository.delete(saveUser);

        Optional<User> findUser = userRepository.findById(saveUser.getId());

        assertThat(findUser).isNotPresent();
    }

    @Test
    @DisplayName("User 저장 후 User id로 조회 테스트")
    void findByUserId() {
        User saveUser = userRepository.save(UserTest.JAVAJIGI);
        Optional<User> findUser = userRepository.findByUserId(saveUser.getUserId());

        assertThat(findUser).isPresent();
        assertThat(findUser.get()).isSameAs(saveUser);
    }
}
