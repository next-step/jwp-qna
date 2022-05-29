package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setup(){
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
    }

    @AfterEach
    void clean(){
        userRepository.deleteAll();
    }

    @Test
    void save() {
        User expected = UserTest.ROCKPRO87;
        User actual = userRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId())
        );
    }

    @Test
    void findByUserId() {
        String expected = UserTest.JAVAJIGI.getUserId();
        User actual = userRepository.findByUserId(expected).get();
        assertThat(actual.getUserId()).isEqualTo(expected);
    }

}
