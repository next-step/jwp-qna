package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;
import qna.domain.UserTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    public User userTest;

    @BeforeEach
    void setUp() {
        userTest = UserTest.JAVAJIGI;
    }

    @Test
    @DisplayName("User 저장한 엔티티의 id로 조회한 경우 동일성 테스트")
    void find() {
        User saveUser = userRepository.save(userTest);
        User findQuestion = userRepository.findById(saveUser.getId()).orElse(null);
        assertThat(saveUser).isEqualTo(findQuestion);
    }
}
