package qna.domain.user.userid;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.repository.UserRepository;
import qna.domain.user.User;
import qna.domain.user.email.Email;
import qna.domain.user.name.Name;
import qna.domain.user.password.Password;

@DataJpaTest
class UserIdTest {

    @Autowired
    UserRepository users;

    @Autowired
    TestEntityManager em;

    @DisplayName("userId가 중복되면 EX 발생")
    @Test
    void unique() {
        User user = users.save(getUser(null, "writer", "1111", "작성자", "writer@naver.com"));
        flushAndClear();
        assertThatIllegalArgumentException().isThrownBy(() -> new UserId(user.getUserId(), users));
    }

    @DisplayName("제한 된 길이를 넘어가면 EX 발생")
    @ParameterizedTest
    @CsvSource(value = {"1:123456789123456789123"}, delimiter = ':')
    void length(String success, String ex) {
        assertThatNoException().isThrownBy(() -> new UserId(success));
        assertThatIllegalArgumentException().isThrownBy(() -> new UserId(ex));
    }

    @DisplayName("null 이거나 empty 이면 EX 발생")
    @ParameterizedTest
    @NullAndEmptySource
    void nullAndEmpty(String userId) {
        assertThatIllegalArgumentException().isThrownBy(() -> new UserId(userId));
    }

    private User getUser(Long id, String userId, String password, String name, String email) {
        return new User(id, new UserId(userId, users), new Password(password), new Name(name), new Email(email));
    }

    private void flushAndClear() {
        em.flush();
        em.clear();
    }
}
