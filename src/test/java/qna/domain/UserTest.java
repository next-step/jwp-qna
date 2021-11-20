package qna.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {

	public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
	public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

	@Test
	@DisplayName("ID가 같다면 객체도 같아야 한다")
	void equalsTest() {
		// given
		User user1 = new User(1L, "chaeyun", "pw", "chaeyun", "chaeyun@naver.com");
		User user2 = new User(1L, "chaeyun2", "pw2", "chaeyun2", "chaeyun2@naver.com");

		// when, then
		assertThat(user1).isEqualTo(user2)
			.hasSameHashCodeAs(user2);
	}

}
