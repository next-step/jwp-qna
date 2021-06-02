package qna.domain;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import qna.config.TestDataSourceConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestDataSourceConfig
class UserTest {
    public static final User JAVAJIGI = new User(100L, "javajigi", "password", "name1", "javajigi@slipp.net");
    public static final User SANJIGI = new User(200L, "sanjigi", "password", "name2", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @DisplayName("save 검증")
    @MethodSource("testcase")
    @ParameterizedTest
    void saveTest(User user) {
        equals(user, userRepository.save(user));
    }

    @DisplayName("save & findByUserId 검증")
    @MethodSource("testcase")
    @ParameterizedTest
    void findByUserIdTest(User user) {
        userRepository.save(user);
        equals(user, userRepository.findByUserId(user.getUserId())
                                   .orElseThrow(IllegalArgumentException::new));
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> testcase() {
        return Stream.of(Arguments.of(JAVAJIGI), Arguments.of(SANJIGI));
    }

    private void equals(User expected, User actual) {
        assertEquals(expected.getUserId(), actual.getUserId());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getEmail(), actual.getEmail());
    }
}
