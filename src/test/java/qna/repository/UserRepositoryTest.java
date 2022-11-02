package qna.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;
import qna.domain.UserTest;
import qna.repository.UserRepository;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    
    @DisplayName("유저 식별자로 유저 조회")
    @Test
    void findByUserId() {
        User user = userRepository.save(UserTest.JAVAJIGI);

        Optional<User> optionalUser = userRepository.findByUserId(user.getUserId());

        assertAll(
            () -> assertThat(optionalUser.isPresent()).isTrue(),
            () -> assertThat(optionalUser.get()).isEqualTo(user)
        );
    }
}