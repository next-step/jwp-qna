package qna.domain;

public class UserTest {
    public static final User JAVAJIGI = new User.UserBuilder("javajigi", "password", "name")
            .id(1L)
            .email("javajigi@slipp.net")
            .build();
    public static final User SANJIGI = new User.UserBuilder("sanjigi", "password", "name")
            .id(2L)
            .email("sanjigi@slipp.net")
            .build();
}
