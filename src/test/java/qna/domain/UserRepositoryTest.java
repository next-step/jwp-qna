package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @DisplayName("id를 통한 검색 테스트")
    @Test
    void findById() {
        Optional<User> optionalUser = userRepository.findById(1L);
        assertThat(optionalUser.isPresent()).isTrue();

        User user = optionalUser.get();
        assertAll(
                () -> assertThat(user.getId()).isEqualTo(1L),
                () -> assertThat(user.getPassword()).isEqualTo("abcd"),
                () -> assertThat(user.getName()).isEqualTo("안드로이드"),
                () -> assertThat(user.getUserId()).isEqualTo("user1"),
                () -> assertThat(user.getEmail()).isNull(),
                () -> assertThat(user.getCreatedAt()).isNotNull()
        );

    }

    @DisplayName("userId를 통한 검색 테스트")
    @Test
    void findByUserId() {
        Optional<User> optionalUser = userRepository.findByUserId("user2");
        assertThat(optionalUser.isPresent()).isTrue();

        User user = optionalUser.get();
        assertAll(
                () -> assertThat(user.getId()).isEqualTo(2L),
                () -> assertThat(user.getPassword()).isEqualTo("efgh"),
                () -> assertThat(user.getName()).isEqualTo("터미네이터"),
                () -> assertThat(user.getUserId()).isEqualTo("user2"),
                () -> assertThat(user.getEmail()).isNull(),
                () -> assertThat(user.getCreatedAt()).isNotNull()
        );
    }

    @DisplayName("User 정보 저장 테스트")
    @Test
    void saveUser() {
        userRepository.save(new User("user3", "aaww", "건담", "ssiaa@naver.com"));

        Optional<User> optionalUser = userRepository.findByUserId("user3");
        assertThat(optionalUser.isPresent()).isTrue();

        User user = optionalUser.get();
        assertAll(
                () -> assertThat(user.getPassword()).isEqualTo("aaww"),
                () -> assertThat(user.getName()).isEqualTo("건담"),
                () -> assertThat(user.getUserId()).isEqualTo("user3"),
                () -> assertThat(user.getEmail()).isEqualTo("ssiaa@naver.com"),
                () -> assertThat(user.getCreatedAt()).isNotNull()
        );
    }

    @DisplayName("User 정보 업데이트 테스트")
    @Test
    void updateUser() {
        User savedUser = userRepository.save(new User("user3", "aaww", "건담", "ssiaa@naver.com"));
        LocalDateTime firstTime = savedUser.getUpdatedAt();
        userRepository.flush();
        savedUser.update(savedUser, new User("user3", "aaww", "김건담", "gundamkim@naver.com"));
        userRepository.flush();

        Optional<User> optionalUser = userRepository.findByUserId("user3");
        assertThat(optionalUser.isPresent()).isTrue();

        User user = optionalUser.get();
        assertAll(
                () -> assertThat(user.getUserId()).isEqualTo("user3"),
                () -> assertThat(user.getName()).isEqualTo("김건담"),
                () -> assertThat(user.getEmail()).isEqualTo("gundamkim@naver.com"),
                () -> assertThat(user.getUpdatedAt()).isNotEqualTo(firstTime)
        );

    }


}