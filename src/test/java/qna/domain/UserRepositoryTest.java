package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository repository;

    @Test
    void insert_이후_select() {
        // given
        final User user = new User("ttungga", "password", "old name", "old email");

        // when
        repository.save(user);

        // then
        assertThat(user.getId()).isNotNull();
        assertThat(user).isEqualTo(repository.findById(user.getId()).get());
    }

    @Test
    void update_이후_select() {
        // given
        final User user = new User("ttungga", "password", "old name", "old email");
        repository.save(user);
        final String newName = "new name";
        final String newEmail = "new email";
        final User updatedUser = new User("ttungga", "password", newName, newEmail);

        // when
        user.update(user, updatedUser);

        // then
        assertThat(user.getName()).isEqualTo(newName);
        assertThat(user.getEmail()).isEqualTo(newEmail);
        assertThat(user.getName()).isEqualTo(repository.findById(user.getId()).get().getName());
        assertThat(user.getEmail()).isEqualTo(repository.findById(user.getId()).get().getEmail());
    }

    @Test
    void userId를_통해_사용자를_조회할_수_있어야_한다() {
        // given
        final User user = new User("ttungga", "password", "old name", "old email");
        repository.save(user);

        // when
        final User selected = repository.findByUserId(user.getUserId()).get();

        // then
        assertThat(selected).isNotNull();
        assertThat(selected).isEqualTo(user);
    }
}
