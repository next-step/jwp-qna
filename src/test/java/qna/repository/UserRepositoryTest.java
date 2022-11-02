package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
@DisplayName("User")
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("저장")
    public void save() {
        User target = UserTest.JAVAJIGI;
        User saved = repository.save(target);
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getUserId()).isEqualTo(target.getUserId()),
                () -> assertThat(saved.getEmail()).isEqualTo(target.getEmail()),
                () -> assertThat(saved.getName()).isEqualTo(target.getName()),
                () -> assertThat(saved.getPassword()).isEqualTo(target.getPassword())
        );
    }

    @Test
    @DisplayName("개별 조회 by id")
    public void findById() {
        User saved = repository.save(UserTest.JAVAJIGI);
        Optional<User> fetched = repository.findById(saved.getId());
        assertThat(fetched).isNotEmpty();
        assertThat(fetched.get()).isEqualTo(saved);
    }

    @Test
    @DisplayName("개별 조회 by userId")
    public void findByUserId() {
        User target = UserTest.JAVAJIGI;
        repository.save(target);
        Optional<User> fetched = repository.findByUserId(target.getUserId());
        assertThat(fetched).isNotEmpty();
    }

    @Test
    @DisplayName("제거")
    public void delete() {
        User saved = repository.save(UserTest.JAVAJIGI);
        repository.delete(saved);
        Optional<User> fetched = repository.findById(saved.getId());
        assertThat(fetched).isEmpty();
    }
}
