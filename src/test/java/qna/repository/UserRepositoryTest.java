package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import qna.config.JpaAuditConfig;
import qna.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

@Import(JpaAuditConfig.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("주어진 유저를 영속화한다")
    void save_user_test() {
        User user = new User("shshon", "password1", "name", "email@naver.com");
        userRepository.save(user);
        assertThat(user.getId()).isNotNull();
    }

    @Test
    @DisplayName("주어진 User ID 값으로 유저를 조회한다")
    void find_user_with_user_id_test() {
        User expectedUser = new User("shshon", "password1", "name", "email@naver.com");
        expectedUser = userRepository.save(expectedUser);

        User user = userRepository.findByUserId(expectedUser.getUserId()).get();

        assertThat(user).isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("주어진 Name 값으로 유저를 조회한다")
    void find_user_with_name_test() {
        User expectedUser = new User("shshon", "password1", "name", "email@naver.com");
        expectedUser = userRepository.save(expectedUser);

        User user = userRepository.findByName("name");

        assertThat(user).isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("주어진 Email 값으로 유저를 조회한다")
    void find_user_with_email_test() {
        User expectedUser = new User("shshon", "password1", "name", "email@naver.com");
        expectedUser = userRepository.save(expectedUser);

        User user = userRepository.findByEmail("email@naver.com");

        assertThat(user).isEqualTo(expectedUser);
    }
}
