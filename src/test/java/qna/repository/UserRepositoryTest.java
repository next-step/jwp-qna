package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    User user1;
    User user2;

    @BeforeEach
    void setUp() {
        user1 = User.builder("javajigi", "password", "name")
                .email("javajigi@slipp.net")
                .build();
        user2 = User.builder("sanjigi", "password", "name")
                .email("sanjigi@slipp.net")
                .build();
    }

    @DisplayName("저장 테스트")
    @Test
    void save() {
        User user = userRepository.save(user1);
        assertAll(
                () -> assertThat(user.getId()).isNotNull(),
                () -> assertThat(user.getEmail()).isEqualTo(user1.getEmail()),
                () -> assertThat(user.getName()).isEqualTo(user1.getName()),
                () -> assertThat(user.getPassword()).isEqualTo(user1.getPassword())
        );
    }

    @DisplayName("유저 ID 로 조회 테스트")
    @Test
    void findByUserId() {
        User user = userRepository.save(user1);
        userRepository.save(user2);
        Optional<User> actual = userRepository.findByUserId("javajigi");
        assertThat(actual.orElse(null)).isEqualTo(user);
    }

    @DisplayName("delete 가 잘 되었는지 테스트")
    @Test
    void delete() {
        User User = userRepository.save(user2);
        userRepository.delete(User);
        Optional<User> user = userRepository.findByUserId("javajigi");
        assertThat(user.orElse(null)).isNull();
    }
}
