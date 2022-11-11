package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.domain.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;

@DisplayName("유저 Repository")
class UserRepositoryTest extends RepositoryTest{

    @Autowired
    private UserRepository userRepository;

    @DisplayName("저장_성공")
    @Test
    void save_success() {

        User user = userRepository.save(JAVAJIGI);

        assertAll(
                () -> assertThat(user.getId()).isNotNull(),
                () -> assertThat(user.getName()).isEqualTo(JAVAJIGI.getName()),
                () -> assertThat(user.getEmail()).isEqualTo(JAVAJIGI.getEmail()),
                () -> assertThat(user.getPassword()).isEqualTo(JAVAJIGI.getPassword()),
                () -> assertThat(user.getUserId()).isEqualTo(JAVAJIGI.getUserId()),
                () -> assertThat(user.getCreatedAt()).isNotNull(),
                () -> assertThat(user.getUpdatedAt()).isNotNull()
        );
    }

    @DisplayName("findByUserId_조회_성공")
    @Test
    void findByUserId() {


        User user = userRepository.findByUserId(sanjigi.getUserId()).orElse(null);

        assertAll(
                () -> assertThat(user.getId()).isNotNull(),
                () -> assertThat(user.getName()).isEqualTo(sanjigi.getName()),
                () -> assertThat(user.getEmail()).isEqualTo(sanjigi.getEmail()),
                () -> assertThat(user.getPassword()).isEqualTo(sanjigi.getPassword()),
                () -> assertThat(user.getUserId()).isEqualTo(sanjigi.getUserId()),
                () -> assertThat(user.getCreatedAt()).isNotNull(),
                () -> assertThat(user.getUpdatedAt()).isNotNull()
        );
    }
}
