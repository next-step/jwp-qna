package qna.domain;

public class UserFixture {
    private static final User javajigi = new User(null, "javajigi", "password", "name", "javajigi@slipp.net");

    public static User javajigi() {
        return javajigi;
    }

}
