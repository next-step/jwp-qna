package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class UserRepositoryTest {

	@Autowired
	private UserRepository users;

	@Test
	@DisplayName("userId 로 한 명의 User 를 가지고 올 수 있다.")
	void findByUserIdTest() {
		String expectedUserId = "userId";
		User testUser = new User(expectedUserId, "1234", "name", "email");
		users.save(testUser);

		User findUser = users.findByUserId(expectedUserId).orElseThrow(IllegalArgumentException::new);

		assertAll(
			() -> assertThat(findUser.getId()).isEqualTo(testUser.getId()),
			() -> assertThat(findUser.getUserId()).isEqualTo(expectedUserId)
		);
	}
}