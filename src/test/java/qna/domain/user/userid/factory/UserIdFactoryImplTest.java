package qna.domain.user.userid.factory;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.repository.UserRepository;
import qna.domain.user.User;
import qna.domain.user.email.Email;
import qna.domain.user.name.Name;
import qna.domain.user.password.Password;
import qna.domain.user.userid.UserId;

@DataJpaTest
class UserIdFactoryImplTest {

    @Autowired
    UserRepository users;

    @Test
    @DisplayName("이미 존재하는 userId를 입력하면 EX 발생")
    void unique_userId() {
        users.save(new User(new UserId("ex"), new Password("1111"), new Name("오류발생ID"),
                new Email("ex@naver.com")));
        UserIdFactory factory = new UserIdFactoryImpl(users);
        assertThatNoException().isThrownBy(() -> factory.create("success"));
        assertThatIllegalArgumentException().isThrownBy(() -> factory.create("ex"));
    }

}
