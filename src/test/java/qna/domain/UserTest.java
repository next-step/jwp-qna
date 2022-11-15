package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @DisplayName("저장하면 DB가 생성한 아이디가 있다.")
    @Test
    void save() {
        final User newUser = new User("dominiqn", "password", "남동민", "dmut7691@gmail.com");
        final User savedUser = userRepository.save(newUser);

        assertThat(savedUser.getId()).isNotNull();
    }

    @DisplayName("동일한 유저아이디를 두 개 이상 저장할 수 없다. 유저아이디는 유일해야 한다.")
    @Test
    void uniqueUserId() {
        final User newUser = new User("dominiqn", "password", "남동민", "dmut7691@gmail.com");
        userRepository.save(newUser);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> {
                    final User duplicatedUser = new User("dominiqn", "password", "남동민", "dmut7691@gmail.com");
                    userRepository.save(duplicatedUser);
                });
    }
}
