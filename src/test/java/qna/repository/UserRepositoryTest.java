package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;

@DataJpaTest
public class UserRepositoryTest {
    public static final User JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @Nested
    @DisplayName("명령")
    class Command {
        @Test
        @DisplayName("새로운 유저를 추가한다.")
        void save() {
            User actual = userRepository.save(SANJIGI);
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getName()).isEqualTo(SANJIGI.getName()),
                    () -> assertThat(actual.getPassword()).isEqualTo(SANJIGI.getPassword()),
                    () -> assertThat(actual.getUserId()).isEqualTo(SANJIGI.getUserId())
            );
        }
    }

    @Nested
    @DisplayName("조회")
    class Query {
        @BeforeEach
        void setUp() {
            userRepository.saveAll(Arrays.asList(JAVAJIGI, SANJIGI));
        }

        @Test
        @DisplayName("UserId로 유저를 찾는다.")
        void findByUserId() {
            Optional<User> actual = userRepository.findByUserId(JAVAJIGI.getUserId());
            assertAll(
                    () -> assertThat(actual).isNotEmpty(),
                    () -> assertThat(actual).map(User::getName).hasValue(JAVAJIGI.getName()),
                    () -> assertThat(actual).map(User::getPassword).hasValue(JAVAJIGI.getPassword())
            );
        }
    }
}
