package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;
import qna.domain.UserRepository;
import qna.domain.UserTest;

@DisplayName("Answer_관련_테스트")
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @DisplayName("save_확인")
    @Test
    void save() {
        User result = userRepository.save(UserTest.JAVAJIGI);
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getUserId()).isEqualTo(UserTest.JAVAJIGI.getUserId()),
                () -> assertThat(result.getPassword()).isEqualTo(UserTest.JAVAJIGI.getPassword()),
                () -> assertThat(result.getName()).isEqualTo(UserTest.JAVAJIGI.getName()),
                () -> assertThat(result.getEmail()).isEqualTo(UserTest.JAVAJIGI.getEmail())
        );
    }

    @DisplayName("findByUserId_user_id_기준으로_User데이터_조회")
    @Test
    void findByUserId_01() {
        User result = userRepository.save(UserTest.JAVAJIGI);

        assertThat(userRepository.findByUserId(result.getUserId()).orElse(null)).isEqualTo(result);
    }
}
