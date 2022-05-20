package qna.domain;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Order(2)
    @Test
    void user_저장() {
        User user = UserTest.JAVAJIGI;
        User result = userRepository.save(user);

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getName()).isEqualTo(user.getName())
        );
    }

    @Order(1)
    @Test
    void user_전체_조회() {
        userRepository.save(UserTest.SANJIGI);
        userRepository.save(UserTest.JAVAJIGI);

        assertThat(userRepository.findAll()).hasSize(2);
    }

    @Order(3)
    @Test
    void user_단건_조회() {
        User user = userRepository.save(UserTest.JAVAJIGI);

        assertThat(userRepository.findById(user.getId()).get())
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Order(4)
    @Test
    void user_단건_조회_findByUserId() {
        User user = userRepository.save(UserTest.JAVAJIGI);

        assertThat(userRepository.findByUserId(user.getUserId()).get())
                .usingRecursiveComparison()
                .isEqualTo(user);
    }
}
