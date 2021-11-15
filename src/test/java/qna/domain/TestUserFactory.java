package qna.domain;

public class TestUserFactory {

    private TestUserFactory() {

    }

    public static User create(String userId) {
        return new User(userId, "password", "name", "name@email.com");
    }

}
