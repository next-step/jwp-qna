package qna.repos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserRepositoryTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository repository;

    @DisplayName("User 저장 테스트")
    @Test
    void save() {
        User expected = JAVAJIGI;
        User actual = repository.save(JAVAJIGI);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId())
        );
    }

    @DisplayName("User Id 테스트")
    @Test
    void findByUserId() {
        User user = repository.save(JAVAJIGI);

        repository.save(user);

        Optional<User> userResult = repository.findByUserId(user.getUserId());
        Optional<User> user2Result = repository.findByUserId(SANJIGI.getUserId());

        assertAll(
                () -> assertThat(userResult).isNotEmpty(),
                () -> assertThat(user2Result).isEmpty()
        );
    }
}
