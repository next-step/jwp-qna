package qna.fixture;

import qna.domain.User;

public class UserFixture {
    private UserFixture() {
        throw new UnsupportedOperationException();
    }

    public static User ID가_없는_사용자() {
        return new User("user", "password", "name", "user@slipp.net");
    }

    public static User ID가_없는_다른_사용자() {
        return new User("otherUser", "password", "name", "otherUser@slipp.net");
    }

    public static User ID가_있는_사용자() {
        return new User(1L, "user", "password", "name", "user@slipp.net");
    }

    public static User ID가_있는_다른_사용자() {
        return new User(2L, "otherUser", "password", "name", "otherUser@slipp.net");
    }
}
