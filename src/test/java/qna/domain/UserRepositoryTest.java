package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("사용자 저장소 테스트")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void 테스트_수행_전_데이터_일괄삭제() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("사용자 저장")
    void 저장() {
        User user = UserTest.JAVAJIGI;
        User saved = userRepository.save(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(user.getUserId()).isEqualTo(saved.getUserId());
        assertThat(user.getEmail()).isEqualTo(saved.getEmail());
        assertThat(user.getName()).isEqualTo(saved.getName());
        assertThat(user.getPassword()).isEqualTo(saved.getPassword());
        assertThat(user.isGuestUser()).isEqualTo(saved.isGuestUser());
    }

    @Test
    @DisplayName("사용자 수정")
    void 수정() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        user.setName("윤채은");
        User updatedUser = userRepository.findById(user.getId()).get();

        assertThat(updatedUser.getName()).isEqualTo(user.getName());
    }

    @Test
    @DisplayName("사용자 삭제")
    void 삭제() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        userRepository.delete(user);

        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

    @Test
    @DisplayName("사용자 USER ID로 사용자 조회")
    void 사용자_USER_ID로_사용자_조회() {
        User saved = userRepository.save(UserTest.JAVAJIGI);
        User expected = userRepository.findByUserId(saved.getUserId()).get();

        assertThat(saved.getId()).isEqualTo(expected.getId());
        assertThat(saved.getUserId()).isEqualTo(expected.getUserId());
        assertThat(saved.getEmail()).isEqualTo(expected.getEmail());
        assertThat(saved.getName()).isEqualTo(expected.getName());
        assertThat(saved.getPassword()).isEqualTo(expected.getPassword());
        assertThat(saved.isGuestUser()).isEqualTo(expected.isGuestUser());
    }
}
