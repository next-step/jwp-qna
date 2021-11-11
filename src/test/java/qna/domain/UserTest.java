package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void save() {
        User expected = new User("javajigi", "password", "name", "javajigi@slipp.net");

        User actual = userRepository.save(expected);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getUserId()).isEqualTo(expected.getUserId());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
        assertThat(actual.equalsNameAndEmail(expected)).isTrue();
    }

    @Test
    @DisplayName("JPA가 식별자가 같은 엔티티에 대한 동일성을 보장하는지 테스트")
    void identity() {
        User expected = saveNewDefaultUser();

        User actual = userRepository.findById(expected.getId()).get();

        assertThat(actual == expected).isTrue();
    }

    @Test
    @DisplayName("JPA 변경감지로 인한 업데이트 기능 테스트")
    void update() {
        User expected = saveNewDefaultUser();
        expected.setName("임민석");
        expected.setEmail("minseoklim@slipp.net");

        User actual = userRepository.findById(expected.getId()).get();

        assertThat(actual.equalsNameAndEmail(expected)).isTrue();
    }

    @Test
    @DisplayName("ID로 삭제 후, 조회가 되지 않는지 테스트")
    void delete() {
        User expected = saveNewDefaultUser();
        userRepository.deleteById(expected.getId());

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
            () -> userRepository.findById(expected.getId()).get()
        );
    }

    @Test
    void findByUserId() {
        User expected = saveNewDefaultUser();

        User actual = userRepository.findByUserId(expected.getUserId()).get();

        assertThat(actual == expected).isTrue();
    }

    private User saveNewDefaultUser() {
        User defaultUser = new User("javajigi", "password", "name", "javajigi@slipp.net");
        return userRepository.save(defaultUser);
    }
}
