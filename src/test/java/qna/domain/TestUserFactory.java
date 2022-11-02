package qna.domain;

public class TestUserFactory {

    public static User create(String userId) {
        return new User(userId, "password", "name", userId + "@gmail.com");
    }
}
