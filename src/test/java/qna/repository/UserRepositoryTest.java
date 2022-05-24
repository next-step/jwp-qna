package qna.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;

import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저를 등록 후 정상적으로 등록되었는지 확인 테스트")
    void save() {
        userRepository.save(SANJIGI);

        Assertions.assertThat(userRepository.findAll()).size().isEqualTo(1);
    }

    @Test
    @DisplayName("유저를 등록 후 정상적으로 조회되었는지 확인 테스트")
    void find() {
        User result = userRepository.save(JAVAJIGI);
        User user = userRepository.findByUserId(result.getUserId()).get();

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getId()).isEqualTo(result.getId());
    }

}
