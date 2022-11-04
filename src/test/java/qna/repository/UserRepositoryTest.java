package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;
import qna.domain.UserTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @DisplayName("사용자를 저장할 수 있다")
    @Test
    void save() {
        User actual = userRepository.save(UserTest.JAVAJIGI);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getUserId()).isEqualTo(actual.getUserId()),
                () -> assertThat(actual.getName()).isEqualTo(actual.getName()),
                () -> assertThat(actual.getPassword()).isEqualTo(actual.getPassword())
        );
    }

    @DisplayName("사용자 계정으로 조회할 수 있다")
    @Test
    void findByUserId() {
        User actual = userRepository.save(UserTest.JAVAJIGI);

        User result = userRepository.findByUserId(actual.getUserId()).get();

        assertThat(actual == result).isTrue();
    }
}
