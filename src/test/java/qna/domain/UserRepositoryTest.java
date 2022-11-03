package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("사용자 저장소 테스트")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자 저장")
    void 저장() {
        User user = UserTestFixture.JAVAJIGI;
        User saved = userRepository.save(user);

        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(user.getUserId()).isEqualTo(saved.getUserId()),
                () -> assertThat(user.getEmail()).isEqualTo(saved.getEmail()),
                () -> assertThat(user.getName()).isEqualTo(saved.getName()),
                () -> assertThat(user.getPassword()).isEqualTo(saved.getPassword()),
                () -> assertThat(user.isGuestUser()).isEqualTo(saved.isGuestUser())
        );
    }

    @Test
    @DisplayName("사용자 수정")
    void 수정() {
        User user = userRepository.save(UserTestFixture.JAVAJIGI);
        User target = new User(user.getId(), user.getUserId(), user.getPassword(), "윤채은", user.getEmail());
        user.update(user, target);

        User updatedUser = userRepository.findById(user.getId()).get();

        assertThat(updatedUser.getName()).isEqualTo(user.getName());
    }

    @Test
    @DisplayName("사용자 삭제")
    void 삭제() {
        User user = userRepository.save(UserTestFixture.JAVAJIGI);
        userRepository.delete(user);

        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

    @Test
    @DisplayName("사용자 USER ID로 사용자 조회")
    void 사용자_USER_ID로_사용자_조회() {
        User saved = userRepository.save(UserTestFixture.JAVAJIGI);
        User expected = userRepository.findByUserId(saved.getUserId()).get();

        assertAll(
                () -> assertThat(saved.getId()).isEqualTo(expected.getId()),
                () -> assertThat(saved.getUserId()).isEqualTo(expected.getUserId()),
                () -> assertThat(saved.getEmail()).isEqualTo(expected.getEmail()),
                () -> assertThat(saved.getName()).isEqualTo(expected.getName()),
                () -> assertThat(saved.getPassword()).isEqualTo(expected.getPassword()),
                () -> assertThat(saved.isGuestUser()).isEqualTo(expected.isGuestUser())
        );
    }
}
