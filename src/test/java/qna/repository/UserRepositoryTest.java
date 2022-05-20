package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.AnswerTest;
import qna.domain.User;
import qna.domain.UserTest;

import java.util.Optional;

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
        Optional<User> actual = userRepository.findByUserId(UserTest.JAVAJIGI.getUserId());
        // then
        assertAll(
                () -> assertThat(actual.isPresent()).isTrue(),
                () -> assertThat(actual.get() == user).isTrue()
        );
    }
}