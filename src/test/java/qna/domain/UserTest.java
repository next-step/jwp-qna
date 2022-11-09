package qna.domain;

public class UserTest {
    public static final User JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
    public static final User JAVAJIGI_WITH_ID = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
    public static final User SANJIGI_WITH_ID = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
}
