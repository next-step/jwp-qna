package qna.domain;

public class UserTestFactory {
    private UserTestFactory() {
        throw new UnsupportedOperationException();
    }

    public static User create(Long id, String userId) {
        return new User(id, userId, "password", "name", userId + "@slipp.net");
    }
}
