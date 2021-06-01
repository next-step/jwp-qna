package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name",
        "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name",
        "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자 정보 저장")
    void save() {
        User result = userRepository.save(JAVAJIGI);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(JAVAJIGI.getName());
        assertThat(result.getUserId()).isEqualTo(JAVAJIGI.getUserId());
        assertThat(result.getPassword()).isEqualTo(JAVAJIGI.getPassword());
        assertThat(result.getEmail()).isEqualTo(JAVAJIGI.getEmail());
    }

    @Test
    @DisplayName("사용자ID로 사용자 정보 조회")
    void findByUserId() {
        User user = userRepository.save(JAVAJIGI);
        User result = userRepository.findByUserId(user.getUserId()).get();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getPassword()).isEqualTo(user.getPassword());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getUserId()).isEqualTo(user.getUserId());
        assertThat(result.getName()).isEqualTo(user.getName());
    }
}
