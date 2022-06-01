package qna.domain;

public class UserFixtures {
    public static User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    public static User create(String userId, String password, String name, String email) {
        return new User(userId, password, name, email);
    }
}
