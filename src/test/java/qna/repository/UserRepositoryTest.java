package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
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

    public static final User JAVAJIGI = new User.UserBuilder("javajigi", "password", "name")
            .id(1L)
            .email("javajigi@slipp.net")
            .build();
    public static final User SANJIGI = new User.UserBuilder("sanjigi", "password", "name")
            .id(2L)
            .email("sanjigi@slipp.net")
            .build();

    @DisplayName("저장 테스트")
    @Test
    void save() {
        User user = userRepository.save(JAVAJIGI);
        assertAll(
                () -> assertThat(user.getId()).isNotNull(),
                () -> assertThat(user.getEmail()).isEqualTo(JAVAJIGI.getEmail()),
                () -> assertThat(user.getName()).isEqualTo(JAVAJIGI.getName()),
                () -> assertThat(user.getPassword()).isEqualTo(JAVAJIGI.getPassword())
        );
    }

    @DisplayName("유저 ID 로 조회 테스트")
    @Test
    void findByUserId() {
        User user = userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
        Optional<User> actual = userRepository.findByUserId("javajigi");
        assertThat(actual.orElse(null)).isEqualTo(user);
    }

    @DisplayName("delete 가 잘 되었는지 테스트")
    @Test
    void delete() {
        User User = userRepository.save(JAVAJIGI);
        userRepository.delete(User);
        Optional<User> user = userRepository.findByUserId("javajigi");
        assertThat(user.orElse(null)).isNull();
    }
}
