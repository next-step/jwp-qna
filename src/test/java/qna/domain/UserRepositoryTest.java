package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("USER가 잘 저장되는지 확인한다.")
    @Test
    void saveUserTest() {

        User saveUser1 = userRepository.save(UserTest.JAVAJIGI);
        User saveUser2 = userRepository.save(UserTest.SANJIGI);
        assertAll(
                () -> assertThat(saveUser1.getId()).isNotNull(),
                () -> assertThat(saveUser1.getName()).isEqualTo(UserTest.JAVAJIGI.getName()),
                () -> assertThat(saveUser2.getId()).isNotNull(),
                () -> assertThat(saveUser2.getEmail()).isEqualTo(UserTest.SANJIGI.getEmail())
        );
    }

    @DisplayName("USER ID로 조회되는지 확인한다.")
    @Test
    void findByUserId() {

        User saveUser = userRepository.save(UserTest.JAVAJIGI);
        assertSame(userRepository.findByUserId(UserTest.JAVAJIGI.getUserId()).get(), saveUser);

    }
}
