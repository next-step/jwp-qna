package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import qna.config.JpaAuditingConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(value = {JpaAuditingConfiguration.class})
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("null 값 저장시 예외 테스트")
    public void save() {
        assertThatThrownBy(() -> userRepository.save(new User(3L, "javajigi", null, "name", "test@test.test")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("unique 값 저장시 예외 테스트")
    public void uniqueTest() {
        User user1 = userRepository.save(JAVAJIGI);
        User user2 = userRepository.save(SANJIGI);
        user2.setUserId(user1.getUserId());
        assertThatThrownBy(() -> userRepository.flush()).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("존재하지 않는 데이터 조회 테스트")
    public void findByUserIdTestNotExists() {
        userRepository.save(JAVAJIGI);
        Optional<User> byUserId = userRepository.findByUserId(SANJIGI.getUserId());
        assertThat(byUserId).isEmpty();
    }

    @Test
    @DisplayName("존재하는 데이터 조회 테스트")
    public void findByUserIdTestExists() {
        userRepository.save(JAVAJIGI);
        Optional<User> byUserId = userRepository.findByUserId(JAVAJIGI.getUserId());
        assertThat(byUserId).isNotEmpty();
    }
}