package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository users;

    @DisplayName("User 저장")
    @Test
    void saveUser() {
        User user = users.save(TestCreateFactory.createUser(1L));

        Long id = user.getId();

        assertThat(id).isNotNull();
    }
}
