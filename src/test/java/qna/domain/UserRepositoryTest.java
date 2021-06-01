package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository users;

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "JavaJiGi", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "SamJiGi", "sanjigi@slipp.net");

    @DisplayName("User 저장 테스트")
    @Test
    void save() {
        User actual = users.save(JAVAJIGI);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.equalsNameAndEmail(JAVAJIGI)).isTrue()
        );
    }

    @DisplayName("User에 JAVAJIGI를 저장 후 name 조회")
    @Test
    void findByName() {
        users.save(JAVAJIGI);
        User user = users.findByName("JavaJiGi");
        assertThat(user).isNotNull();
        assertThat(user.matchPassword("password")).isTrue();
    }

    @DisplayName("User 에 JAVAJIGIFMF 저장 후 SANJIGI 로 업데이트 테스트")
    @Test
    void update() {
        User actual = users.save(JAVAJIGI);
        User expected = users.findByName("JavaJiGi");
        assertThat(expected.getName()).isEqualTo("JavaJiGi");

        expected.setName("SanJiGi");
        User expected2 = users.findByName("JavaJiGi");
        assertThat(expected2).isNull();
    }

    @DisplayName("User 에 JAVAJIGI를 저장 후 다시 delete")
    @Test
    void delete() {
        User actual = users.save(JAVAJIGI);
        User expected = users.findByName("JavaJiGi");
        assertThat(expected.getName()).isEqualTo("JavaJiGi");

        users.delete(actual);
        User expected2 = users.findByName("JavaJiGi");
        assertThat(expected2).isNull();
    }
}
