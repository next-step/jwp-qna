package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저를 저장한다.")
    void save_user_test() {
        User user = new User("javajigi", "password", new Name("name"), "javajigi@slipp.net");

        User savedUser = userRepository.save(user);

        assertAll(
            () -> assertThat(savedUser.getId()).isNotNull(),
            () -> assertThat(savedUser.getUserId()).isEqualTo(user.getUserId()),
            () -> assertThat(savedUser.getPassword()).isEqualTo(user.getPassword()),
            () -> assertThat(savedUser).isEqualTo(user),
            () -> assertThat(savedUser.getEmail()).isEqualTo(user.getEmail())
        );
    }

    @Test
    @DisplayName("유저 ID로 유저를 조회한다.")
    void query_user_by_user_id_test() {
        User savedUser = userRepository.save(new User("sanjigi", "password", new Name("name"), "sanjigi@slipp.net"));

        User findUser = userRepository.findByUserId(savedUser.getUserId()).get();

        assertThat(findUser).isEqualTo(savedUser);
    }

    @Test
    @DisplayName("ID로 유저를 조회한다.")
    void query_user_by_id_test() {
        User savedUser = userRepository.save(new User("javajigi", "password", new Name("name"), "javajigi@slipp.net"));

        User findUser = userRepository.findById(savedUser.getId()).get();

        assertThat(findUser).isEqualTo(savedUser);
    }

    @Test
    @DisplayName("저장된 유저의 이름을 변경한다.")
    void change_user_name_test() {
        User javajigi = new User("test", "password", new Name("name"), "javajigi@slipp.net");
        User savedUser = userRepository.save(javajigi);

        javajigi.changeName("넥스트스텝");
        savedUser.update(savedUser, javajigi);
        User findUser = userRepository.findById(savedUser.getId()).get();

        assertThat(findUser).isEqualTo(savedUser);
    }

    @Test
    @DisplayName("유저 정보 업데이트 시 동일한 유저가 아닌 경우 exception이 발생한다.")
    void update_user_info_exception_test() {
        final User dummyUser = new User("testId", "2345", new Name("dummy"), "dummy@example.com");
        final User user = userRepository.save(dummyUser);

        assertThatThrownBy(() ->
            user.update(UserTest.JAVAJIGI, dummyUser)
        ).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("유저를 삭제한다.")
    void delete_test() {
        User expected = userRepository.save(new User("testId123", "23a45", new Name("test"), "test@test.com"));

        userRepository.delete(expected);

        assertThat(userRepository.findById(expected.getId())).isEmpty();
    }

    @Test
    @DisplayName("이미 등록된 ID인 경우 exception이 발생한다.")
    void duplicate_test() {
        User user = userRepository.save(new User("testId123", "23a45", new Name("test"), "test@test.com"));

        assertThatThrownBy(() -> {
            final User newUser = new User(user.getUserId(), "1234", new Name("test"), "test@gmail.com");
            userRepository.save(newUser);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
}
