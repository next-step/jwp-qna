package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("유저 저장")
    void save_user() {
        User saveUser = userRepository.save(JAVAJIGI);
        assertAll(
                () -> assertThat(saveUser.getId()).isNotNull(),
                () -> assertThat(saveUser.getId()).isEqualTo(JAVAJIGI.getId()),
                () -> assertThat(saveUser.getUserId()).isEqualTo(JAVAJIGI.getUserId()),
                () -> assertThat(saveUser.getPassword()).isEqualTo(JAVAJIGI.getPassword()),
                () -> assertThat(saveUser.getName()).isEqualTo(JAVAJIGI.getName()),
                () -> assertThat(saveUser.getEmail()).isEqualTo(JAVAJIGI.getEmail())
        );
    }
    
    @Test
    @DisplayName("유저 조회")
    void find_user() {
        userRepository.save(SANJIGI);
        Optional<User> findUser = userRepository.findByUserId(SANJIGI.getUserId());
        Assertions.assertTrue(findUser.isPresent());
        findUser.ifPresent(sanjigi ->
                assertAll(
                        () -> assertThat(sanjigi.getId()).isNotNull(),
                        () -> assertThat(sanjigi.getUserId()).isEqualTo(SANJIGI.getUserId()),
                        () -> assertThat(sanjigi.getPassword()).isEqualTo(SANJIGI.getPassword()),
                        () -> assertThat(sanjigi.getName()).isEqualTo(SANJIGI.getName()),
                        () -> assertThat(sanjigi.getEmail()).isEqualTo(SANJIGI.getEmail())
                )
        );
    }
}
