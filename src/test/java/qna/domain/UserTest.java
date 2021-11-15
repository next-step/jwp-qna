package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @DisplayName("Create 및 ID 생성 테스트")
    @Test
    void save() {
        User save = userRepository.save(JAVAJIGI);
        assertThat(save.getId()).isNotNull();
    }

    @DisplayName("Read 테스트")
    @Test
    void read() {
        User save = userRepository.save(JAVAJIGI);
        User found = userRepository.findById(save.getId()).orElse(null);
        assertThat(found).isEqualTo(save);
    }

    @DisplayName("Update 테스트")
    @Test
    void update() {
        User save = userRepository.save(JAVAJIGI);
        save.setUserId("steadyjin");
        User found = userRepository.findById(save.getId()).orElseThrow(() -> new NullPointerException("테스트실패"));
        assertThat(found.getUserId()).isEqualTo("steadyjin");
    }

    @DisplayName("Delete 테스트")
    @Test
    void delete() {
        User save = userRepository.save(JAVAJIGI);
        userRepository.delete(save);
        userRepository.flush();
        User found = userRepository.findById(save.getId()).orElse(null);
        assertThat(found).isNull();
    }
}
