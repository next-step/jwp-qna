package qna.domain;

public class UserTestFactory {
    private UserTestFactory() {
        throw new UnsupportedOperationException();
    }

    public static User create(String userId) {
        return create(null, userId);
    }

    public static User create(Long id, String userId) {
        return new User(id, userId, "password", "name", userId + "@slipp.net");
    }
}
