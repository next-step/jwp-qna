package qna.repository;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    public static final User JAVAJIGI = new User.UserBuilder("javajigi", "password", "name")
            .id(1L)
            .email("javajigi@slipp.net")
            .build();
    public static final User SANJIGI = new User.UserBuilder("sanjigi", "password", "name")
            .id(2L)
            .email("sanjigi@slipp.net")
            .build();
}
