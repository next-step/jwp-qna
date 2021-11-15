package qna.fixture;

import qna.domain.User;

public class UserFixture {
	public static User 식별자가_userId인_유저(String userId) {
		return new User(userId, "password", "name", "e@mail.com");
	}
}
