package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository users;

    @Test
    void save() {
        final User expected = UserTest.JAVAJIGI;
        final User actual = users.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId())
        );
    }

    @Test
    void findByUserIdTest() {
        String expected = UserTest.JAVAJIGI.getUserId();
        users.save(UserTest.JAVAJIGI);
        String actual = users.findByUserId(expected).get().getUserId();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void test() {
    }
}
