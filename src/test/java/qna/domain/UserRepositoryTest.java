package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void save() {
        final User user = userRepository.save(UserTest.SANJIGI);
        assertThat(user.getUserLogin()).isEqualTo(UserTest.SANJIGI.getUserLogin());
    }

    @Test
    @DisplayName("save 학습테스트. GenerationType.IDENTITY 전략에서 객체를 저장하였을 때 결과 확인")
    void saveIDENTITY() {
        final User user = userRepository.save(UserTest.TESTUSER);
        assertThat(user.getId()).isNotEqualTo(UserTest.TESTUSER.getId());
        //UserTest.TESTUSER 의 id를 지정해줬어도 IDENTITY 에 따라 실제 id 저장 값은 다르게 된다.
    }

    @Test
    @DisplayName("회원정보를 통해서 user 를 찾는다")
    void findByUserId() {
        final User user = userRepository.save(UserTest.JAVAJIGI);
        final Optional<User> findUser = userRepository.findByUserLogin(user.getUserLogin());
        assertThat(findUser.get().getUserLogin()).isEqualTo(user.getUserLogin());
    }
}