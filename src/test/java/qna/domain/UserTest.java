package qna.domain;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("user 엔티티 테스트")
@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
    public static final User MINGVEL = new User(3L, "mingvel", "password", "name", "dlsqo2005@naver.com");

    @Autowired
    private UserRepository userRepository;

    @DisplayName("생성 성공")
    @Test
    void save_user_success() {
        assertThatNoException().isThrownBy(() -> userRepository.save(MINGVEL));
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

    @DisplayName("userId 가 null 인 엔티티 저장")
    @Test
    void saveNullUserId_user_DataIntegrityViolationException() {
        //given:
        User user = new User(1L, null, "password", "name", "dlsqo2005@naver.com");
        //when, then:
        assertThatThrownBy(() -> userRepository.save(user))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("password 가 null 인 엔티티 저장")
    @Test
    void saveNullPassword_user_DataIntegrityViolationException() {
        //given:
        User user = new User(1L, "mingvel", null, "name", "dlsqo2005@naver.com");
        //when, then:
        assertThatThrownBy(() -> userRepository.save(user))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("name 이 null 인 엔티티 저장")
    @Test
    void saveNullName_user_DataIntegrityViolationException() {
        //given:
        User user = new User(1L, "mingvel", "password", null, "dlsqo2005@naver.com");
        //when, then:
        assertThatThrownBy(() -> userRepository.save(user))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("길이가 20 이상인 userId 를 가지는 엔티티 저장")
    @Test
    void saveOverLengthUserId_user_DataIntegrityViolationException() {
        //given:
        User user = new User(1L, RandomString.make(21), "password", "name", "dlsqo2005@naver.com");
        //when, then:
        assertThatThrownBy(() -> userRepository.save(user))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("길이가 20 이상인 password 를 가지는 엔티티 저장")
    @Test
    void saveOverLengthPassword_user_DataIntegrityViolationException() {
        //given:
        User user = new User(1L, "mingvel", RandomString.make(21), "name", "dlsqo2005@naver.com");
        //when, then:
        assertThatThrownBy(() -> userRepository.save(user))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("길이가 20 이상인 name 을 가지는 엔티티 저장")
    @Test
    void saveOverLengthName_user_DataIntegrityViolationException() {
        //given:
        User user = new User(1L, "mingvel", "password", RandomString.make(21), "dlsqo2005@naver.com");
        //when, then:
        assertThatThrownBy(() -> userRepository.save(user))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("길이가 50 이상인 email 을 가지는 엔티티 저장")
    @Test
    void saveOverLengthEmail_user_DataIntegrityViolationException() {
        //given:
        User user = new User(1L, "mingvel", "password", "name", RandomString.make(51));
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
}
