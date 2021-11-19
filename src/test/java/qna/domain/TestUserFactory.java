package qna.domain;

public class TestUserFactory {
    public static User create() {
        return new User("javajigi", "password", "name", "javajigi@slipp.net");
    }
}

