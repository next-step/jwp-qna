package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @DisplayName("동등성 비교 테스트")
    @Test
    void identityTest() {
        final User savedUser = userRepository.save(UserTest.JAVAJIGI);
        Optional<User> isUser = userRepository.findByUserId(savedUser.getUserId());
        assertThat(isUser.isPresent()).isTrue();
        assertThat(isUser.get()).isSameAs(savedUser);
    }

    @DisplayName("User 찾기 테스트")
    @Test
    void findByUserId() {
        List<User> savedUser = userRepository.saveAll(Arrays.asList(UserTest.SANJIGI, UserTest.JAVAJIGI));
        Optional<User> isUser = userRepository.findByUserId(UserTest.JAVAJIGI.getUserId());
        assertThat(isUser.isPresent()).isTrue();
        assertThat(savedUser).contains(isUser.get());
    }
}
