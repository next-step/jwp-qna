package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.User;

@DataJpaTest
@DisplayName("User")
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private TestEntityManager testEntityManager;

    private User user;

    @BeforeEach
    public void init() {
        user = new User("taewon", "password", "name", "htw1800@naver.com");
    }

    @Test
    @DisplayName("저장")
    public void save() {
        User saved = saveAndRefetch(user);
        assertAll(
                () -> assertThat(saved).isEqualTo(user),
                () -> assertThat(saved.getUserId()).isEqualTo(user.getUserId()),
                () -> assertThat(saved.getEmail()).isEqualTo(user.getEmail()),
                () -> assertThat(saved.getName()).isEqualTo(user.getName()),
                () -> assertThat(saved.getPassword()).isEqualTo(user.getPassword())
        );
    }

    @Test
    @DisplayName("개별 조회 by id")
    public void findById() {
        User saved = saveAndClear(user);
        Optional<User> optional = repository.findById(saved.getId());
        assertThat(optional).isNotEmpty();
        User fetched = optional.get();
        assertThat(fetched).isEqualTo(saved);
    }

    @Test
    @DisplayName("개별 조회 by userId")
    public void findByUserId() {
        User saved = saveAndClear(user);
        Optional<User> optional = repository.findByUserId(saved.getUserId());
        assertThat(optional).isNotEmpty();
        User fetched = optional.get();
        assertThat(fetched).isEqualTo(saved);
        assertThat(fetched.getUserId()).isEqualTo(saved.getUserId());
    }

    @Test
    @DisplayName("제거")
    public void delete() {
        User saved = saveAndRefetch(user);
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
