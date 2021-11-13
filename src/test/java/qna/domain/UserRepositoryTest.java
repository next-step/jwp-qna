package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

	@Autowired
	UserRepository users;

	@Test
	@DisplayName("저장하면 ID가 생성되어야 한다")
	void saveTest() {

		// given
		User user1 = new User("javajigi", "password", "name", "javajigi@slipp.net");

		// when
		user1 = users.save(user1);

		// then
		assertThat(user1.getId()).isNotNull();
	}

	@Test
	@DisplayName("유저를 유저아이디로 조회할 수 있어야 한다")
	void findByUserIdTest() {
		// given
		User user1 = new User("javajigi", "password", "name", "javajigi@slipp.net");
		User user2 = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
		user1 = users.save(user1);
		user2 = users.save(user2);

		// when
		Optional<User> result = users.findByUserId(user1.getUserId());

		// then
		User finalUser = user1;
		assertAll(
			() -> assertThat(result).isPresent(),
			() -> assertThat(result).containsSame(finalUser)
		);
	}

	@Test
	@DisplayName("유저를 모두 조회할 수 있어야 한다")
	void findAllTest() {
		// given
		User user1 = new User("javajigi", "password", "name", "javajigi@slipp.net");
		User user2 = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
		users.save(user1);
		users.save(user2);

		// when
		List<User> result = users.findAll();

		// then
		assertAll(
			() -> assertThat(result.size()).isEqualTo(2),
			() -> assertThat(result).contains(user1, user2)
		);
	}
}
