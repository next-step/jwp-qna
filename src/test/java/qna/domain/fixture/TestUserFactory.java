package qna.domain.fixture;

import qna.domain.User;

public class TestUserFactory {

    public static User create(final String user) {
        return new User(user, "password", "lsh", "lsh@mail.com");
    }
}
