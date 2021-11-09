package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("User 테스트")
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    private static Stream<Arguments> testAnswers() {
        return Stream.of(Arguments.of(JAVAJIGI), Arguments.of(SANJIGI));
    }

    @DisplayName("Save 확인")
    @ParameterizedTest(name = "{displayName} ({index}) -> param = [{arguments}]")
    @MethodSource("testAnswers")
    void save_확인(User expectedResult) {
        User result = userRepository.save(expectedResult);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(expectedResult.getId()),
                () -> assertThat(result.getUserId()).isEqualTo(expectedResult.getUserId()),
                () -> assertThat(result.getPassword()).isEqualTo(expectedResult.getPassword()),
                () -> assertThat(result.getName()).isEqualTo(expectedResult.getName()),
                () -> assertThat(result.getEmail()).isEqualTo(expectedResult.getEmail())
        );
    }
}
