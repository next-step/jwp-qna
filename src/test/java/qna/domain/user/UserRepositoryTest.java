package qna.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.user.UserTest.JAVAJIGI;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import qna.NotFoundException;

@DirtiesContext
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("사용자가 정상적으로 등록되있는지 테스트 한다")
    void saveUserTest(){
        User user = userRepository.save(JAVAJIGI);
        assertAll(
            () -> assertThat(user.getId()).isEqualTo(JAVAJIGI.getId()),
            () -> assertThat(user.getUserId()).isEqualTo(JAVAJIGI.getUserId()),
            () -> assertThat(user.getName()).isEqualTo(JAVAJIGI.getName()),
            () -> assertThat(user.getPassword()).isEqualTo(JAVAJIGI.getPassword()),
            () -> assertThat(user.getEmail()).isEqualTo(JAVAJIGI.getEmail()),
            () -> assertThat(user.getCreatedAt()).isEqualTo(JAVAJIGI.getCreatedAt()),
            () -> assertThat(user.getUpdatedAt()).isEqualTo(JAVAJIGI.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("사용자를 등록하고 조회되는지 테스트한다")
    void byIdAndDeletedFalseTest(){
        User user = userRepository.save(JAVAJIGI);
        User getUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException());
        assertAll(
                () -> assertThat(getUser).isNotNull(),
                () -> assertThat(getUser.getId()).isEqualTo(JAVAJIGI.getId()),
                () -> assertThat(getUser.getUserId()).isEqualTo(JAVAJIGI.getUserId()),
                () -> assertThat(getUser.getName()).isEqualTo(JAVAJIGI.getName()),
                () -> assertThat(getUser.getPassword()).isEqualTo(JAVAJIGI.getPassword()),
                () -> assertThat(getUser.getEmail()).isEqualTo(JAVAJIGI.getEmail()),
                () -> assertThat(getUser.getCreatedAt()).isEqualTo(JAVAJIGI.getCreatedAt()),
                () -> assertThat(getUser.getUpdatedAt()).isEqualTo(JAVAJIGI.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("사용자를 id로 삭제되는지 테스트한다")
    void deleteByIdTest(){
        User user = userRepository.save(JAVAJIGI);
        userRepository.deleteById(user.getId());
        Optional<User> getUser = userRepository.findById(user.getId());
        assertThat(getUser.isPresent()).isFalse();
    }

}
