package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("userId를 올바른 user를 찾는다.")
    void find_user_with_user_id() {
        User requestedUser = userRepository.save(UserTest.JAVAJIGI);
        User foundUser = userRepository.findByUserId(requestedUser.getUserId())
            .orElseThrow(NotFoundException::new);
        assertThat(foundUser.equals(requestedUser)).isTrue();
    }

}