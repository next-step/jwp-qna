package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository users;

    private static Stream<User> testFixtureProvider() {
        return Stream.of(JAVAJIGI, SANJIGI);
    }

    @ParameterizedTest
    @MethodSource("testFixtureProvider")
    void find(final User user) {
        final User saved = users.save(user);
        final User actual = users.findById(saved.getId()).get();
        assertThat(actual).isEqualTo(saved);
        assertThat(actual).isSameAs(saved);
    }

    @ParameterizedTest
    @MethodSource("testFixtureProvider")
    void update(final User user) {
        final User saved = users.save(user);

        // Update name and email
        User updated = users.findById(saved.getId()).get();
        updated.update(updated, new User(saved.getUserId(), saved.getPassword(), "withbeth", "wbg"));

        final User actual = users.findById(saved.getId()).get();
        assertThat(actual.getName()).isEqualTo("withbeth");
        assertThat(actual.getEmail()).isEqualTo("wbg");
    }

    @ParameterizedTest
    @MethodSource("testFixtureProvider")
    void delete(final User user) {
        final User saved = users.save(user);

        users.deleteById(saved.getId());
        assertThat(users.findById(saved.getId()).orElse(null)).isNull();
    }

}
