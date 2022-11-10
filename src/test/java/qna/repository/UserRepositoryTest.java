package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DisplayName("유저 Repository")
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User sanjigi;

    @BeforeEach
    void setUp() {
        sanjigi = userRepository.save(SANJIGI);
    }

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
    void findByUserId_success() {

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
