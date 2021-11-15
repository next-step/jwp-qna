package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository users;

    @DisplayName("사용자 저장 테스트")
    @Test
    void saveUserTest() {
        final User savedUser1 = users.save(JAVAJIGI);
        final User savedUser2 = users.save(SANJIGI);

        assertAll(
                    () -> assertThat(savedUser1.getId()).isNotNull(),
                    () -> assertThat(savedUser1.getUserId()).isEqualTo(JAVAJIGI.getUserId()),
                    () -> assertThat(savedUser1.getPassword()).isEqualTo(JAVAJIGI.getPassword()),
                    () -> assertThat(savedUser1.getName()).isEqualTo(JAVAJIGI.getName()),
                    () -> assertThat(savedUser1.getEmail()).isEqualTo(JAVAJIGI.getEmail())
                );

        assertAll(
                () -> assertThat(savedUser2.getId()).isNotNull(),
                () -> assertThat(savedUser2.getUserId()).isEqualTo(SANJIGI.getUserId()),
                () -> assertThat(savedUser2.getPassword()).isEqualTo(SANJIGI.getPassword()),
                () -> assertThat(savedUser2.getName()).isEqualTo(SANJIGI.getName()),
                () -> assertThat(savedUser2.getEmail()).isEqualTo(SANJIGI.getEmail())
        );
    }

    @DisplayName("사용자 조회 테스트")
    @Test
    void findByUserIdTest() {
        final User savedUser1 = users.save(JAVAJIGI);
        final User savedUser2 = users.save(SANJIGI);

        final User actualUser1 = users.findByUserId(JAVAJIGI.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("user가 존재하지 않습니다."));

        final User actualUser2 = users.findByUserId(SANJIGI.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("user가 존재하지 않습니다."));

        assertThat(actualUser1).isEqualTo(savedUser1);
        assertThat(actualUser1).isSameAs(savedUser1);
        assertThat(actualUser1.getId()).isSameAs(savedUser1.getId());

        assertThat(actualUser2).isEqualTo(savedUser2);
        assertThat(actualUser2).isSameAs(savedUser2);
        assertThat(actualUser2.getId()).isSameAs(savedUser2.getId());
    }

    @DisplayName("사용자 수정 테스트")
    @Test
    void updateUserTest() {
        final User savedUser1 = users.save(JAVAJIGI);

        savedUser1.setName("JAVA");
        final User actualUser1 = users.findByName("JAVA")
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        assertThat(actualUser1.getName()).isEqualTo("JAVA");
    }
}
