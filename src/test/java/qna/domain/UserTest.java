package qna.domain;

public class UserTest {
    public static final User JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");

    public static User newUser(String appendString) {
        return new User("user" + appendString, "password" + appendString, "name" + appendString,
            appendString + "email@test.com");
    }
}
