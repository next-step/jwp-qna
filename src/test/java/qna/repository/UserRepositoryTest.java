package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @DisplayName("사용자를 저장할 수 있다")
    @Test
    void save() {
        User actual = userRepository.save(JAVAJIGI);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getUserId()).isEqualTo(JAVAJIGI.getUserId()),
                () -> assertThat(actual.getName()).isEqualTo(JAVAJIGI.getName()),
                () -> assertThat(actual.getPassword()).isEqualTo(JAVAJIGI.getPassword()),
                () -> assertThat(actual.getEmail()).isEqualTo(JAVAJIGI.getEmail())
        );
    }

    @DisplayName("사용자 계정으로 조회할 수 있다")
    @Test
    void findByUserId() {
        User actual = userRepository.save(JAVAJIGI);

        User result = userRepository.findByUserId(actual.getUserId()).get();

        assertThat(actual == result).isTrue();
    }
}
