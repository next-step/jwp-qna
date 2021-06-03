package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository users;
    private User user;

    @BeforeEach
    void setUp() {
        user = users.save(UserTest.JAVAJIGI);
    }

    @Test
    void findByUserId() {
        User expected = users.findByUserId(UserTest.JAVAJIGI.getUserId()).get();

        assertAll(
                () -> assertThat(expected).isNotNull(),
                () -> assertThat(expected.getUserId()).isEqualTo(UserTest.JAVAJIGI.getUserId())
        );
    }
}