package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("UserRepository 클래스")
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRespotiroy;

    @DisplayName("저장")
    @Test
    void save() {
        final User saved = userRespotiroy.save(UserTest.JAVAJIGI);
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getCreatedAt()).isNotNull(),
                () -> assertThat(saved.getUpdatedAt()).isNotNull()
        );
    }

    @DisplayName("User Id 조회")
    @Test
    void findByUserId() {
        final User saved = userRespotiroy.save(UserTest.JAVAJIGI);
        final User finded = userRespotiroy.findByUserId(saved.getUserId()).get();
        assertThat(saved.equalsNameAndEmail(finded)).isTrue();
    }

    @DisplayName("name email 변경")
    @Test
    void update() {
        final User saved = userRespotiroy.save(UserTest.JAVAJIGI);
        saved.update(saved, UserTest.JAVAJIGI2);
        final User finded = userRespotiroy.findByUserId(saved.getUserId()).get();

        assertAll(
                () -> assertThat(finded.getName()).isEqualTo(UserTest.JAVAJIGI2.getName()),
                () -> assertThat(finded.getEmail()).isEqualTo(UserTest.JAVAJIGI2.getEmail())

        );
    }
}
