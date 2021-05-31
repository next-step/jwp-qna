package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User javajigi;

    private User saved;

    @BeforeEach
    void setUp() {
        javajigi = new User("javajigi", "password", "name", "email");
        saved = userRepository.save(javajigi);
    }

    @Test
    @DisplayName("User 저장 테스트")
    void save() {
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved).isSameAs(javajigi),
                () -> assertThat(saved.getUserId()).isEqualTo(javajigi.getUserId()),
                () -> assertThat(saved.getPassword()).isEqualTo(javajigi.getPassword()),
                () -> assertThat(saved.getName()).isEqualTo(javajigi.getName()),
                () -> assertThat(saved.getEmail()).isEqualTo(javajigi.getEmail())
        );
    }

    @Test
    @DisplayName("User 수정 테스트")
    void update() {
        String changedName = "crong";
        saved.updateName(changedName);

        Optional<User> user = userRepository.findById(saved.getId());

        assertThat(user.get().getName()).isEqualTo(changedName);
    }

    @Test
    @DisplayName("User 제거 테스트")
    void delete() {
        userRepository.delete(saved);

        List<User> users = userRepository.findAll();

        assertThat(users.contains(saved)).isFalse();
    }

    @Test
    @DisplayName("userId로 user 조회 테스트")
    void findByUserId() {
        Optional<User> finded = userRepository.findByUserId(javajigi.getUserId());

        assertThat(finded.get()).isEqualTo(javajigi);
    }
}
