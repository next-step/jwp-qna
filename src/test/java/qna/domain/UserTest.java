package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

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
        final User saved = userRepository.save(JAVAJIGI);
        final User found = userRepository.findByUserId("javajigi").orElseGet(()->null);
        assertThat(found).isEqualTo(saved);
    }

    @Test
    @DisplayName("User Entity Update 테스트")
    void update() {
        final User saved = userRepository.save(JAVAJIGI);
        saved.setUserId("jigi2");
        User found = userRepository.findByUserId("jigi2").orElseGet(()->null);
        assertThat(found).isEqualTo(saved);
    }

    @Test
    @DisplayName("User Entity Delete 테스트")
    void delete() {
        final User saved = userRepository.save(JAVAJIGI);
        userRepository.delete(saved);
        userRepository.flush();
        final User found = userRepository.findByUserId("javajigi").orElseGet(()->null);
        assertThat(found).isNull();
    }
}
