package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository users;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = users.save(UserTest.JAVAJIGI);
        user2 = users.save(UserTest.SANJIGI);
    }

    @Test
    void saveTest(){
        assertThat(user1).isNotNull();
    }

    @Test
    void findByUserIdTest(){
        User expectedResult = user1;
        Optional<User> actualResult = users.findByUserId(expectedResult.getUserId());
        assertThat(actualResult).containsSame(expectedResult);
    }

}