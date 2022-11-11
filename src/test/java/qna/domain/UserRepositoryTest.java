package qna.domain;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.ParameterizedTest.DEFAULT_DISPLAY_NAME;

@DataJpaTest
@DisplayName("user 엔티티 테스트")
public class UserRepositoryTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
    public static final User MINGVEL = new User(3L, "mingvel", "password", "name", "dlsqo2005@naver.com");
    public static final User DELETE_SOON_USER = new User(4L, "delete", "password", "deleteSoon", "asdf@naver.com");

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @DisplayName("생성 성공")
    @Test
    void save_user_success() {
        assertThat(userRepository.save(MINGVEL).getId()).isNotNull();
    }

    @DisplayName("findByUserId 메서드 테스트")
    @Test
    void findByUserId_user_success() {
        //given:
        User user = userRepository.save(MINGVEL);
        //when:
        User expected = userRepository.findByUserId(user.getUserId()).orElse(new User());
        //then:
        assertThat(expected.getUserId()).isEqualTo(user.getUserId());
    }

    @DisplayName("updatedAt 데이터 변경 여부 테스트")
    @Test
    void save_answer_changedUpdatedAt() {
        final User user = userRepository.save(MINGVEL);
        final LocalDateTime before = user.getUpdatedAt();
        user.setName("changedName");
        final User afterUser = userRepository.save(user);
        testEntityManager.flush();
        assertThat(afterUser.getUpdatedAt()).isAfter(before);
    }

    @ParameterizedTest(name = "제약 조건 위반 테스트" + DEFAULT_DISPLAY_NAME)
    @MethodSource("invalidUsers")
    void save_user_DataIntegrityViolationException(User user) {
        //when, then:
        assertThatThrownBy(() -> userRepository.save(user))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("동일한 userId 를 가지는 엔티티 저장")
    @Test
    void saveSameUserId_user_DataIntegrityViolationException() {
        //given:
        User user = new User(1L, "duplicated", "password", "name", "dlsqo2005@naver.com");
        User duplicatedUserIdUser = new User(2L, "duplicated", "password", "name", "dlsqo2005@naver.com");
        //when, then:
        assertThatThrownBy(() -> userRepository.saveAll(Arrays.asList(user, duplicatedUserIdUser)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("save - update 동작 테스트")
    @Test
    void saveUpdate_user_success() {
        //given:
        User user = userRepository.save(SANJIGI);
        String modifiedName = "changed name";
        //when:
        user.setName(modifiedName);
        User modifiedUser = userRepository.findById(user.getId()).orElse(new User());
        //then:
        assertThat(modifiedUser.getName()).isEqualTo(modifiedName);
    }

    @DisplayName("delete 메서드 테스트")
    @Test
    void delete_user_success() {
        //given:
        User user = userRepository.save(DELETE_SOON_USER);
        //when:
        userRepository.delete(user);
        User deleted = userRepository.findById(user.getId()).orElse(null);
        assertThat(deleted).isNull();
    }

    static User provideUser() {
        return new User("mingvel", "password", "name", "dlsqo2005@naver.com");
    }

    private static Stream<Arguments> invalidUsers() {
        return Stream.of(
                Arguments.of(new User(1L, null, "password", "name", "dlsqo2005@naver.com")),
                Arguments.of(new User(1L, "mingvel", null, "name", "dlsqo2005@naver.com")),
                Arguments.of(new User(1L, "mingvel", "password", null, "dlsqo2005@naver.com")),
                Arguments.of(new User(1L, RandomString.make(21), "password", "name", "dlsqo2005@naver.com")),
                Arguments.of(new User(1L, "mingvel", RandomString.make(21), "name", "dlsqo2005@naver.com")),
                Arguments.of(new User(1L, "mingvel", "password", RandomString.make(21), "dlsqo2005@naver.com")),
                Arguments.of(new User(1L, "mingvel", "password", "name", RandomString.make(51))
                ));
    }
}
