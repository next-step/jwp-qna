package qna.domain;

import org.junit.jupiter.api.AfterEach;
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

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        users.deleteAll();
    }

    @Test
    void saveTest(){
        User actual = users.save(UserTest.JAVAJIGI);
        assertThat(actual).isNotNull();
    }

    @Test
    void findByUserIdTest(){
        User expectedResult = users.save(UserTest.JAVAJIGI);
        Optional<User> actualResult = users.findByUserId(expectedResult.getUserId());
        assertThat(actualResult).containsSame(expectedResult);
    }

}