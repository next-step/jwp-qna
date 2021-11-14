package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        User user = UserTest.JAVAJIGI;

        User result = userRepository.save(user);

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getPassword()).isEqualTo(result.getPassword())
        );

    }

    @DisplayName("검색 테스트")
    @Test
    void findByUserId() {
        User save = userRepository.save(UserTest.JAVAJIGI);
        User result = userRepository.findByUserId("javajigi").get();

        assertThat(result).isEqualTo(save);
    }

    @DisplayName("삭제 테스트")
    @Test
    void delete() {
        User save = userRepository.save(UserTest.JAVAJIGI);

        userRepository.delete(save);
        userRepository.flush();
        User result = userRepository.findByUserId("javajigi").orElseGet(() -> null);

        assertThat(result).isNull();
    }
}
