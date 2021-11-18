package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository users;

    @DisplayName("User 저장 및 데이터 확인")
    @Test
    void saveUser() {
        final User actual = users.save(UserTest.JAVAJIGI);

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getUserId()).isEqualTo(UserTest.JAVAJIGI.getUserId()),
            () -> assertThat(actual.getPassword()).isEqualTo(UserTest.JAVAJIGI.getPassword()),
            () -> assertThat(actual.getName()).isEqualTo(UserTest.JAVAJIGI.getName()),
            () -> assertThat(actual.getEmail()).isEqualTo(UserTest.JAVAJIGI.getEmail())
        );
    }

    @DisplayName("user_id로 데이터 찾기")
    @Test
    void findByUserId() {
        final User standard = users.save(UserTest.JAVAJIGI);
        final User target = users.findByUserId(standard.getUserId()).get();

        String standardUserId = standard.getUserId();
        String targetUserId = target.getUserId();

        assertThat(standardUserId).isEqualTo(targetUserId);
    }

    @DisplayName("user_id와 password로 데이터 찾기")
    @Test
    void findByUserIdAndPassword() {
        final User standard = users.save(UserTest.JAVAJIGI);
        final User target = users.findByUserIdAndPassword(standard.getUserId(), standard.getPassword());

        Long standardId = standard.getId();
        Long targetId = target.getId();

        assertThat(standardId).isEqualTo(targetId);
    }
}
