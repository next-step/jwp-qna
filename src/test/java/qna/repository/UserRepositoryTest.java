package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;
import qna.domain.UserTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    void setUp() {
        user = new User("javajigi", "password", "name", "javajigi@slipp.net");
        userRepository.save(user);
    }

    @Test
    @DisplayName("userId로 검색하여 user객체를 반환한다")
    void findByUserId_test() {
        // when
        User user = userRepository.findByUserId(UserTest.JAVAJIGI.getUserId()).get();

        // then
        assertAll(
                () -> assertThat(user).isNotNull(),
                () -> assertThat(user.getEmail()).isEqualTo(UserTest.JAVAJIGI.getEmail()),
                () -> assertThat(user.getName()).isEqualTo(UserTest.JAVAJIGI.getName())
        );
    }
}
