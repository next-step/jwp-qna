package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("사용자 테스트")
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("사용자 저장")
    public void save() {
        final String userId = userRepository.save(JAVAJIGI).getUserId();
        assertThat(userId).isEqualTo("javajigi");
    }

    @Test
    @DisplayName("ID로 사용자 조회")
    public void findById() {
        final User user = userRepository.findById(userRepository.save(SANJIGI).getId())
                .orElseThrow(NotFoundException::new);
        assertThat(user.getUserId()).isEqualTo("sanjigi");
    }

    @Test
    @DisplayName("사용자ID로 사용자 조회")
    public void findByUserId() {
        final User javajigi = userRepository.findByUserId(userRepository.save(JAVAJIGI).getUserId())
                .orElseThrow(NotFoundException::new);
        assertThat(javajigi.getUserId()).isEqualTo("javajigi");
    }

}
