package qna.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;
import qna.domain.UserTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void findByUserId_조회_테스트() {
        User user = userRepository.save(UserTest.JAVAJIGI);

        Optional<User> optionalUser = userRepository.findByUserId(user.getUserId());

        assertAll(
                () -> assertThat(optionalUser.isPresent()).isTrue(),
                () -> assertThat(optionalUser.get()).isSameAs(user)
        );
    }
}