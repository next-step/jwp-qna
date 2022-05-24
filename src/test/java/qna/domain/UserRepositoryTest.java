package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveTest() {
        //given
        User expected = UserTest.JAVAJIGI;
        //when
        User actual = userRepository.save(expected);
        //then
        assertThat(actual).isNotNull();
    }

    @Test
    void findByName() {
        //given
        User expected = userRepository.save(UserTest.JAVAJIGI);
        //when
        User actual = userRepository.findByUserId("javajigi").get();
        //then
        assertThat(actual).isEqualTo(expected);
    }
}
