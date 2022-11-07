package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.email.Email;
import qna.domain.user.User;
import qna.domain.user.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserTest {

    public static final User JAVAJIGI = new User("javajigi", "password", "name", new Email ("javajigi@slipp.net")); //셀렉트 없이 인설트
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", new Email("sanjigi@slipp.net")); //셀렉트 하고 인설트 id값이 존재 하기때문에

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void 저장() {
        user1 = userRepository.save(JAVAJIGI);
        user2 = userRepository.save(SANJIGI);
        assertThat(user1.getId()).isNotNull();
        assertThat(user2.getId()).isNotNull();
    }

    @Test
    void 조회() {
        List<User> expectList = userRepository.findAll();
        assertAll(
                () -> assertThat(expectList).isNotNull(),
                () -> assertThat(expectList.size()).isEqualTo(2),
                () -> assertThat(user1.getEmail()).isEqualTo(JAVAJIGI.getEmail()),
                () -> assertThat(user2.getEmail()).isEqualTo(SANJIGI.getEmail())
        );
    }

    @Test
    void 수정() {

        Email previousEmail = JAVAJIGI.getEmail();
        Email targetEmail = Email.of("wootechcamp@hotmail.com");
        assertThat(user1.getEmail()).isEqualTo(previousEmail);

        user1.setEmail(targetEmail);
        assertThat(userRepository.save(user1).getEmail()).isEqualTo(targetEmail);
    }

    @Test
    void 삭제() {
        userRepository.deleteAll();

        assertThat(userRepository.findAll().size()).isEqualTo(0);
    }
}
