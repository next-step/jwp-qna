package qna.fixtures;

import qna.domain.User;

public class UserTestFixture {

    public static User createUser() {
        return new User("shshon", "password", "손상훈", "shshon@naver.com");
    }

    public static User createUserWithId(Long id) {
        return new User(id, "shshon", "password", "손상훈", "shshon@naver.com");
    }
}
