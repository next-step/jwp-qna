package qna.domain;

public class UserFixture {
    private static final User javajigi = new User(null, "javajigi", "password", "name", "javajigi@slipp.net");
    private static final User sanjigi = new User(null, "sanjigi", "password", "name", "sanjigi@slipp.net");

    public static User javajigi() {
        return javajigi;
    }

    public static User sanjigi() {
        return sanjigi;
    }
}
