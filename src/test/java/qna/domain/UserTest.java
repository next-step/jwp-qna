package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.annotation.DataJpaTestIncludeAuditing;

@DataJpaTestIncludeAuditing
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @Test
    void save_테스트() {
        User merged = userRepository.save(JAVAJIGI);
        Assertions.assertAll(
                () -> assertThat(merged == JAVAJIGI).isFalse()
        );
    }

    @Test
    void findById_테스트() {
        User merged = userRepository.save(JAVAJIGI);
        Optional<User> u1 = userRepository.findById(merged.getId());
        assertThat(u1.isPresent()).isTrue();
        assertThat(u1.get() == merged).isTrue();
    }

    @Test
    void deleteById_테스트() {
        User merged = userRepository.save(JAVAJIGI);
        userRepository.deleteById(merged.getId());
        Optional<User> u1 = userRepository.findById(merged.getId());
        assertThat(u1.isPresent()).isFalse();
    }

    @Test
    void findByUserId_테스트() {
        User m1 = userRepository.save(JAVAJIGI);
        User m2 = userRepository.save(SANJIGI);
        Optional<User> u1 = userRepository.findByUserId("sanjigi");
        assertThat(u1.isPresent() && u1.get() == m2).isTrue();
    }
}
