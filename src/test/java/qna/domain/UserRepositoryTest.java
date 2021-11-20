package qna.domain;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class UserRepositoryTest {

    @Autowired
    protected UserRepository userRepository;

    @Test
    void 유저_이메일_및_이름이_변경되는지_확인() {
        // given
        User user = new User("seunghoona", "password", "username", "email");
        userRepository.save(user);

        // when
        user.updateNameAndEmail(user, new User(user.getUserId(),user.getPassword(), "javajigi","javajigi@gmail.com"));

        User expectedUser = userRepository.findById(user.getId()).get();

        // then
        Assertions.assertThat(expectedUser.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(expectedUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    @DisplayName("이름으로 조회시 데이터가 조회 되는지 확인")
    void when_findUserName_then_sameUserName() {
        // given
        User user = new User("seunghoona", "password", "username", "email");
        userRepository.save(user);
        // when
        User expectedUser = userRepository.findByName(user.getName()).get();

        // then
        Assertions.assertThat(expectedUser.getName()).isEqualTo(user.getName());
    }

    @Test
    @DisplayName("유저 비밀번호를 변경시 변경되는지 확인")
    void given_user_whenChangePassword_then_isTrue() {
        // given
        User user = new User("seunghoona", "password", "username", "email");
        userRepository.save(user);

        String changePassword = "changePassword";

        // when
        user.changePassword(user, changePassword);
        User expectedUser = userRepository.findByUserId(user.getUserId()).get();

        // then
        Assertions.assertThat(expectedUser.getPassword().equals(changePassword)).isTrue();
    }
}
