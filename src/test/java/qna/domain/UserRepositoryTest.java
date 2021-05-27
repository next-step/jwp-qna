package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository users;

    @Test
    @DisplayName("유저 저장")
    void save() {
        // given
        User user1 = new User("USER1", "123456", "LDS", "lds@test.com");

        // when
        users.save(user1);
        User actual = users.findByUserId("USER1").orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual).isSameAs(user1);
    }
}