package qna.repos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;
import qna.domain.UserTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository repository;

    @DisplayName("User 저장 테스트")
    @Test
    void save() {
        User expected = UserTest.JAVAJIGI;
        User actual = repository.save(UserTest.JAVAJIGI);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId())
        );
    }

    @DisplayName("User Id 테스트")
    @Test
    void findByUserId() {
        //given
        User user = repository.save(UserTest.JAVAJIGI);
        repository.save(user);

        //when
        Optional<User> userResult = repository.findByUserId(user.getUserId());
        Optional<User> user2Result = repository.findByUserId(UserTest.SANJIGI.getUserId());

        //then
        assertAll(
                () -> assertThat(userResult).isNotEmpty(),
                () -> assertThat(user2Result).isEmpty()
        );
    }
}
