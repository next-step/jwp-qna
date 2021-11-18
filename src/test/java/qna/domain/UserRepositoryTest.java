package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("User Entity Create 및 ID 생성 테스트")
    void save() {
        final User user = new User("kim","1q2w3e!","김실장","abc@email.com");
        final User actual = userRepository.save(user);
        assertThat(actual.getId()).isNotNull();

        assertThat(actual.getName()).isEqualTo("김실장");
    }

    @Test
    @DisplayName("User Entity Read 테스트")
    void findById() {
        final User saved = userRepository.save(new User("user3", "password", "name", "user3@slipp.net"));
        final User found = userRepository.findByUserId("user3").orElseGet(()->null);
        assertThat(found).isEqualTo(saved);
    }

    @Test
    @DisplayName("User Entity Update 테스트")
    void update() {
        final User saved = userRepository.save(new User("user3", "password", "name", "user3@slipp.net"));
        saved.setUserId("user3_up");
        final User found = userRepository.findByUserId("user3_up").orElseThrow(() -> new RuntimeException("테스트실패"));
        assertThat(found).isEqualTo(saved);
    }

    @Test
    @DisplayName("User Entity Delete 테스트")
    void delete() {
        final User saved = userRepository.save(new User("user3", "password", "name", "user3@slipp.net"));
        userRepository.delete(saved);
        userRepository.flush();
        final User found = userRepository.findByUserId("user3").orElseGet(()->null);
        assertThat(found).isNull();
    }
}
