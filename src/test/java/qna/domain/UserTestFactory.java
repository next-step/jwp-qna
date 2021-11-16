package qna.domain;

import qna.domain.user.User;

public class UserTestFactory {

    private static final String DUMMY_PASSWORD = "password";
    private static final String DUMMY_NAME = "name";

    public static User create(String userId, String email) {
        return new User(userId, DUMMY_PASSWORD, DUMMY_NAME, email);
    }

    public static User create(Long id, String userId, String email) {
        return new User(id, userId, DUMMY_PASSWORD, DUMMY_NAME, email);
    }

    public static User create(Long id, String userId, String name, String email) {
        return new User(id, userId, DUMMY_PASSWORD, name, email);
    }
}
