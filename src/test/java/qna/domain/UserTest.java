package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserTest {
	public final User JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
	public final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("답변을 저장한다.")
	void save() {
		User actual = userRepository.save(JAVAJIGI);
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getName()).isEqualTo(JAVAJIGI.getName())
		);
	}

	@Test
	@DisplayName("주어진 이름을 가지고 있는 사용자들을 조회한다.")
	void findByName() {
		final User user1 = userRepository.save(JAVAJIGI);
		final User user2 = userRepository.save(SANJIGI);
		final List<User> actual = userRepository.findByName("name");

		assertThat(actual).contains(user1, user2);
	}

	@Test
	@DisplayName("주어진 사용자 Id를 가지고 있는 사용자를 조회한다.")
	void findById() {
		final User user = userRepository.save(JAVAJIGI);
		final User actual = userRepository.findByUserId(user.getUserId()).get();

		assertThat(actual).isEqualTo(user);
	}
}
