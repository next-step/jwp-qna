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
    UserRepository users;

    @DisplayName("User 저장 및 데이터 확인")
    @Test
    void saveUser() {
        final User actual = users.save(JAVAJIGI);

        String userId = actual.getUserId();
        String password = actual.getPassword();

        assertThat(userId).isEqualTo(JAVAJIGI.getUserId());
        assertThat(password).isEqualTo(JAVAJIGI.getPassword());
    }

    @DisplayName("user_id로 데이터 찾기")
    @Test
    void findByUserId() {
        final User standard = users.save(JAVAJIGI);
        final User target = users.findByUserId(JAVAJIGI.getUserId()).get();

        String standardUserId = standard.getUserId();
        String targetUserId = target.getUserId();

        assertThat(standardUserId).isEqualTo(targetUserId);
    }

    @DisplayName("user_id와 password로 데이터 찾기")
    @Test
    void findByUserIdAndPassword() {
        final User standard = users.save(JAVAJIGI);
        final User target = users.findByUserIdAndPassword(JAVAJIGI.getUserId(), JAVAJIGI.getPassword());

        Long standardId = standard.getId();
        Long targetId = target.getId();

        assertThat(standardId).isEqualTo(targetId);
    }
}
