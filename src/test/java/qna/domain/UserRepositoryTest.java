package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User javajigi;
    private User insup;

    @BeforeEach
    void setUp() {
        javajigi = userRepository.save(new User("javajigi", "1234", "javajigi", "a@email.com"));
        insup = userRepository.save(new User("insup", "1234", "insup", "b@email.com"));
    }

    @DisplayName("저장")
    @Test
    void save() {
        assertThat(javajigi).isNotNull();
        assertThat(javajigi.getUserId()).isEqualTo("javajigi");
    }

    @DisplayName("userId로 찾기")
    @Test
    void findByUserId() {
        User findUser = userRepository.findByUserId("javajigi")
                .orElseThrow(NoSuchElementException::new);

        assertThat(findUser).isEqualTo(javajigi);
        assertThat(findUser).isSameAs(javajigi);
    }
}
