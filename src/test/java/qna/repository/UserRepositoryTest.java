package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.annotation.DataJpaTestIncludeAuditing;
import qna.domain.User;
import qna.domain.UserRepository;

@DataJpaTestIncludeAuditing
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void save_테스트() {
        User newUser = new User("javajigi", "password", "name", "javajigi@slipp.net");
        User managedUser = userRepository.save(newUser);
        assertThat(managedUser).isSameAs(newUser);
    }

    @Test
    void findById_테스트() {
        User managedUser = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        Optional<User> foundUser = userRepository.findById(managedUser.getId());
        assertThat(foundUser.isPresent()).isTrue();
    }

    @Test
    void deleteById_테스트() {
        User managedUser = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        userRepository.deleteById(managedUser.getId());
        Optional<User> user = userRepository.findById(managedUser.getId());
        assertThat(user.isPresent()).isFalse();
    }

    @Test
    void findByUserId_테스트() {
        User managedUser1 = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        User managedUser2 = userRepository.save(new User("sanjigi", "password", "name", "sanjigi@slipp.net"));
        Optional<User> sanjigi = userRepository.findByUserId("sanjigi");
        assertThat(sanjigi.isPresent() && sanjigi.get() == managedUser2).isTrue();
    }
}
