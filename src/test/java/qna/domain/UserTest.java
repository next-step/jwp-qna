package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("회원 기능 테스트")
@DataJpaTest
public class UserTest {

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @DisplayName("회원정보 단일 저장을 테스트합니다.")
    @Test
    public void 단일_저장() {
        User savedUser = userRepository.save(JAVAJIGI);

        assertAll(
                () -> assertThat(savedUser).isInstanceOf(User.class).isNotNull(),
                () -> assertThat(savedUser.getId()).isNotNull().isGreaterThan(0L),
                () -> assertThat(savedUser.getUserId()).isNotEmpty().isEqualTo(JAVAJIGI.getUserId())
        );
    }

    @DisplayName("회원정보 목록 저장을 테스트합니다.")
    @Test
    public void 목록_저장() {
        List<User> users = new ArrayList<>();
        users.add(JAVAJIGI);
        users.add(SANJIGI);

        List<User> savedUsers = userRepository.saveAll(users);

        assertAll(
                () -> assertThat(savedUsers).isNotEmpty().hasSize(users.size()),
                () -> savedUsers.forEach(savedUser -> {
                    assertThat(savedUser).isInstanceOf(User.class).isNotNull();
                    assertThat(savedUser.getId()).isNotNull().isGreaterThan(0L);

                    // users 목록의 내용이 저장된게 맞는지 더블체크
                    Optional<User> hasUser = users.stream().filter(user -> user.getId() == savedUser.getId()).findAny();
                    assertThat(hasUser.isPresent()).isTrue();
                })
        );
    }

    @DisplayName("회원정보 조회 성공을 테스트합니다.")
    @Test
    public void 조회_성공() {
        userRepository.save(JAVAJIGI);
        Optional<User> user = userRepository.findByUserId(JAVAJIGI.getUserId());

        assertAll(
                () -> assertThat(user).isPresent(),
                () -> assertThat(user.get().getUserId()).isEqualTo(JAVAJIGI.getUserId())
        );
    }

    @DisplayName("회원정보(id) 조회 실패를 테스트합니다.")
    @Test
    public void id_조회_실패() {
        userRepository.save(JAVAJIGI);

        Optional<User> user = userRepository.findById(0L);
        assertThat(user.isPresent()).isFalse();
    }

    @DisplayName("회원정보(userId) 조회 실패를 테스트합니다.")
    @Test
    public void userId_조회_실패() {
        userRepository.save(JAVAJIGI);

        Optional<User> user = userRepository.findByUserId("");
        assertThat(user.isPresent()).isFalse();
    }

}
