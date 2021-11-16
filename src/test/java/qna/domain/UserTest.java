package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository users;

    @AfterEach
    void tearDown() {
        users.deleteAll();
    }

    @Test
    void save() {
        User expected = new User("javajigi", "password", "name", "javajigi@slipp.net");

        User actual = users.save(expected);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getAccount()).isEqualTo(expected.getAccount());
        assertThat(actual.equalsNameAndEmail(expected)).isTrue();
    }

    @Test
    @DisplayName("JPA가 식별자가 같은 엔티티에 대한 동일성을 보장하는지 테스트")
    void identity() {
        User expected = saveNewDefaultUser();

        User actual = users.findById(expected.getId()).get();

        assertThat(actual == expected).isTrue();
    }

    @Test
    @DisplayName("ID로 삭제 후, 조회가 되지 않는지 테스트")
    void delete() {
        User expected = saveNewDefaultUser();
        users.deleteById(expected.getId());

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
            () -> users.findById(expected.getId()).get()
        );
    }

    @Test
    void findByUserId() {
        User expected = saveNewDefaultUser();

        User actual = users.findByAccount(expected.getAccount()).get();

        assertThat(actual == expected).isTrue();
    }

    private User saveNewDefaultUser() {
        User defaultUser = new User("javajigi", "password", "name", "javajigi@slipp.net");
        return users.save(defaultUser);
    }

    @ParameterizedTest
    @DisplayName("객체 생성 시, not null인 필드에 null이 전달될 경우 예외 발생")
    @MethodSource("provideParametersIncludingNull")
    void createByNull(String userId, String password, String name, String email) {
        assertThatNullPointerException().isThrownBy(() ->
            new User(userId, password, name, email)
        );
    }

    private static Stream<Arguments> provideParametersIncludingNull() {
        return Stream.of(
            Arguments.of(null, "password", "name", "javajigi@slipp.net"),
            Arguments.of("javajigi", null, "name", "javajigi@slipp.net"),
            Arguments.of("javajigi", "password", null, "javajigi@slipp.net")
        );
    }
}
