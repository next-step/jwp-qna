package qna.domain;

import org.junit.jupiter.api.BeforeEach;
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

    private User savedUser;

    @BeforeEach
    void setUp() {
        savedUser = users.save(new User("fdevjc", "password", "yang", "email@email.com"));
    }

    @Test
    @DisplayName("사용자_user_id를 이용하여 사용자를 조회한다.")
    void findByUserId() {
        //when
        User expected = users.findByUserId(savedUser.getUserId()).get();

        //then
        assertAll(
                () -> assertThat(expected).isNotNull(),
                () -> assertThat(expected.getUserId()).isEqualTo(savedUser.getUserId())
        );
    }
}
