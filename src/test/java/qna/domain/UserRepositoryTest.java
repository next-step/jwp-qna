package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @DisplayName("userId로 User 조회")
    @Test
    void findByUserId() {
        //given
        final User expected = userRepository.save(UserTest.JAVAJIGI);

        //when
        final Optional<User> actual = userRepository.findByUserId(expected.getUserId());

        //then
        assertAll(
                () -> assertThat(actual.isPresent()).isTrue(),
                () -> assertThat(actual.get()).isSameAs(expected)
        );
    }
}