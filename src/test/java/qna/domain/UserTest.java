package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class UserTest {

    @Autowired
    private UserRepository userRepository;

    private static Stream<Arguments> providerUsers() {
        User JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
        User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        User UNGSEOK = new User("cus", "JAVA", "최웅석", "웅석_이메일");
        return Stream.of(
                Arguments.of(JAVAJIGI),
                Arguments.of(SANJIGI),
                Arguments.of(UNGSEOK)
        );
    }

    private static Stream<Arguments> providerUngSeok() {
        User UNGSEOK = new User("cus", "JAVA", "최웅석", "웅석_이메일");
        return Stream.of(
                Arguments.of(UNGSEOK)
        );
    }

    @ParameterizedTest
    @MethodSource("providerUsers")
    @DisplayName("저장을 시도하는 객체와 저장후 반환되는 객체가 동일한지 체크한다.")
    public void 유저_생성(User excepted) {
        User actual = userRepository.save(excepted);

        assertThat(actual.equals(excepted)).isTrue();
    }

    @ParameterizedTest
    @DisplayName("최웅석을 저장하고 새로 불러왔지만 이미 최웅석은 캐시 대상이므로 가지고 있는 프록시 객체를 반환한다.")
    @MethodSource("providerUngSeok")
    public void 유저_생성_조회_비교(User ungseok) {
        User excepted = userRepository.save(ungseok);

        Optional<User> actual = userRepository.findByUserId(excepted.getUserId());

        assertThat(excepted).isEqualTo(actual.orElseThrow(() -> new NullPointerException()));
    }

    @Test
    @DisplayName("유저 객체 생성시 아이디 패스워드 검증")
    public void 필수값_검증() {
        assertThatThrownBy(() -> {
            new User(null, "123", "웅석", "웅석이메일");
        }).isInstanceOf(NotFoundException.class);
    }

}