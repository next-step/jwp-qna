package qna.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.repository.UserRepository;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserTest {
    public static final User USER_JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User USER_SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        user1 = userRepository.save(USER_JAVAJIGI);
        user2 = userRepository.save(USER_SANJIGI);
    }

    @Test
    public void exists() {
        assertAll(
            () -> assertThat(user1.getId()).isNotNull(),
            () -> assertThat(user2.getId()).isNotNull()
        );
    }

    @Test
    @DisplayName("동등성 비교")
    public void isEqualTo() {
        assertAll(
            () -> assertThat(user1).isEqualTo(USER_JAVAJIGI),
            () -> assertThat(user2).isEqualTo(USER_SANJIGI)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"javajigi", "sanjigi"})
    public void findByUserId(String userId) {
        //TODO : 피드백 후 삭제하기
        /* Optional 객체에 경우 보통 어떤식으로 테스트를 하는건가요? */

        /*User user = userRepository.findByUserId(userId).orElseThrow(()->new RuntimeException("존재하지 않습니다."));
        assertThat(user.getUserId()).isEqualTo(userId);*/

        Optional<User> userOptional = userRepository.findByUserId(userId);
        assertAll(
            () -> assertThat(userOptional).isNotEmpty(),
            () -> assertThat(userOptional.get().getUserId()).isEqualTo(userId)
        );
    }

    @Test
    @DisplayName("동일성 비교")
    public void isSameAs() {
        User actual1 = userRepository.findByUserId(USER_JAVAJIGI.getUserId()).get();
        User actual2 = userRepository.findByUserId(USER_SANJIGI.getUserId()).get();

        assertAll(
            () -> assertThat(actual1).isSameAs(user1),
            () -> assertThat(actual2).isSameAs(user2)
        );
    }
}