package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저 저장 및 값 비교 테스트")
    void save() {
        final User expected = UserTest.JAVAJIGI;
        final User actual = userRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId()),
                () -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
        );
    }

    @Test
    @DisplayName("userId 조회 테스트")
    void findByUserId() {
        final User expected = userRepository.save(UserTest.JAVAJIGI);
        final Optional<User> actual = userRepository.findByUserId(expected.getUserId());
        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get().getId()).isNotNull(),
                () -> assertThat(actual.get().getUserId()).isEqualTo(expected.getUserId()),
                () -> assertThat(actual.get().getPassword()).isEqualTo(expected.getPassword()),
                () -> assertThat(actual.get().getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.get().getEmail()).isEqualTo(expected.getEmail())
        );
    }
}
