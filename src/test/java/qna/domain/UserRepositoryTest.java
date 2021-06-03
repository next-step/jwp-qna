package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("ID에 해당하는 User를 조회한다.")
    void findByUserId_test() {
        //given
        User javajigi = userRepository.save(JAVAJIGI);

        //when
        Optional<User> findUser = userRepository.findByUserId(javajigi.getUserId());

        //then
        assertThat(findUser.get()).isEqualTo(javajigi);
    }
}
