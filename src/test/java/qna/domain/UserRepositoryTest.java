package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("User 저장시 저장된 User 정보가 저장하려고 했던 User 정보와 일치해야 한다.")
    void saveTest() {
        // given
        User savedUser = userRepository.save(UserTest.JAVAJIGI);

        // when
        User foundUser = userRepository.save(savedUser);

        // then
        assertAll(
            () -> assertThat(foundUser.getId()).isNotNull(),
            () -> assertThat(foundUser.getUserId()).isEqualTo(savedUser.getUserId())
        );
    }

    @Test
    @DisplayName("저장된 User 정보와 조회한 User 정보가 일치해야 한다.")
    void findByUserId() {
        // given
        User savedUser = userRepository.save(UserTest.JAVAJIGI);

        // when
        Optional<User> foundUser = userRepository.findByUserId(savedUser.getUserId());

        // then
        assertAll(
            () -> assertThat(foundUser.isPresent()).isTrue(),
            () -> assertThat(foundUser.orElse(null)).isSameAs(savedUser)
        );
    }

}