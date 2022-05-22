package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {
    private User user;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("vvsungho", "1234", "윤성호", "vvsungho@gmail.com"));
    }

    @Test
    void save() {
        assertThat(user.getId()).isNotNull();
    }

    @Test
    void select() {
        User user2 = userRepository.findByUserId("vvsungho").get();
        assertThat(user).isSameAs(user2);
    }
}
