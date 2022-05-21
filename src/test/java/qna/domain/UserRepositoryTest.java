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
    @DisplayName("User 저장")
    void save(){
        User saved = userRepository.save(UserTest.JAVAJIGI);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("User을 UserId로 조회")
    void User_byUserId(){
        User saved = userRepository.save(UserTest.JAVAJIGI);
        Optional<User> user = userRepository.findByUserId(UserTest.JAVAJIGI.getUserId());
        assertThat(user.get()).isEqualTo(saved);
    }
}
