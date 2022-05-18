package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void user_저장() {
        User user = UserTest.JAVAJIGI;
        User result = userRepository.save(user);

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getName()).isEqualTo(user.getName())
        );
    }

    @Test
    void user_전체_조회() {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);

        assertThat(userRepository.findAll()).hasSize(2);
    }

    @Test
    void user_단건_조회() {
        User user = userRepository.save(UserTest.JAVAJIGI);

        assertThat(userRepository.findById(user.getId()).get())
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void user_단건_조회_findByUserId() {
        User user = userRepository.save(UserTest.JAVAJIGI);

        assertThat(userRepository.findByUserId(user.getUserId()).get())
                .usingRecursiveComparison()
                .isEqualTo(user);
    }
}