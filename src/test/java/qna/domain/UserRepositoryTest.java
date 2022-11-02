package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("입력된 유저아이디에 해당하는 유저를 조회할 수 있어야 한다")
    void findByUserId() {
        //given
        userRepository.save(JAVAJIGI);

        // when
        Optional<User> user = userRepository.findByUserId(JAVAJIGI.getUserId());

        // then
        assertThat(user.get()).usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt")
                .isEqualTo(JAVAJIGI);
    }
}
