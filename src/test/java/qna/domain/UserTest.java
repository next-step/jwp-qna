package qna.domain;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@TestInstance(Lifecycle.PER_CLASS)
@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    UserRepository userRepository;

    @BeforeAll
    private void beforeEach() {
        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
    }

    @DisplayName("특정 유저정보를 조회한다.")
    @Test
    public void find_userInfo() {
        // given
        // when
        Optional<User> realUsers = userRepository.findByUserId("javajigi");

        // then
        Assertions.assertThat(realUsers).isPresent();

        assertAll(
            () -> Assertions.assertThat(realUsers.get().getName()).isEqualTo(JAVAJIGI.getName()),
            () -> Assertions.assertThat(realUsers.get().getPassword()).isEqualTo(JAVAJIGI.getPassword()),
            () -> Assertions.assertThat(realUsers.get().getUserId()).isEqualTo(JAVAJIGI.getUserId()),
            () -> Assertions.assertThat(realUsers.get().getEmail()).isEqualTo(JAVAJIGI.getEmail())
        );
    }
}
