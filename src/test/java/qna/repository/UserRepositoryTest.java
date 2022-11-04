package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
@DisplayName("User")
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("저장")
    public void save() {
        User target = UserTest.JAVAJIGI;
        User saved = saveAndRefetch(target);
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
        User saved = saveAndClear(UserTest.JAVAJIGI);
        Optional<User> optional = repository.findById(saved.getId());
        assertThat(optional).isNotEmpty();
        User fetched = optional.get();
        assertThat(fetched.getId()).isEqualTo(saved.getId());
    }

    @Test
    @DisplayName("개별 조회 by userId")
    public void findByUserId() {
        User saved = saveAndClear(UserTest.JAVAJIGI);
        Optional<User> optional = repository.findByUserId(saved.getUserId());
        assertThat(optional).isNotEmpty();
        User fetched = optional.get();
        assertThat(fetched.getId()).isEqualTo(saved.getId());
        assertThat(fetched.getUserId()).isEqualTo(saved.getUserId());
    }

    @Test
    @DisplayName("제거")
    public void delete() {
        User saved = saveAndRefetch(UserTest.JAVAJIGI);
        repository.delete(saved);
        Optional<User> optional = repository.findById(saved.getId());
        assertThat(optional).isEmpty();
    }

    private User saveAndRefetch(User user) {
        User saved = saveAndClear(user);
        return repository.findById(saved.getId())
                .orElseThrow(() -> new NullPointerException("User not saved!"));
    }

    private User saveAndClear(User user) {
        User saved = repository.save(user);
        testEntityManager.clear();
        return saved;
    }
}
