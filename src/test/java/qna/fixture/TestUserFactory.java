package qna.fixture;

import qna.domain.User;

public class TestUserFactory {
    private TestUserFactory() {}

    public static User create(String name) {
        return new User(name, "password", "userId", "email");
    }
}
