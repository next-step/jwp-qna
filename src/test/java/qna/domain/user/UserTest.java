package qna.domain.user;

import qna.domain.user.email.Email;
import qna.domain.user.name.Name;
import qna.domain.user.password.Password;
import qna.domain.user.userid.UserId;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, new UserId("javajigi"), new Password("password"), new Name("name"),
            new Email("javajigi@slipp.net"));
    public static final User SANJIGI = new User(2L, new UserId("sanjigi"), new Password("password"), new Name("name"),
            new Email("sanjigi@slipp.net"));
}
