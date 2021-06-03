package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserRepositoryTest {
	public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
	public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

	@Autowired
	private UserRepository users;

	@Test
	public void save () {
		User javajigi = users.save(JAVAJIGI);

		assertAll(
				() -> assertThat(javajigi.id()).isNotNull(),
				() -> assertThat(javajigi.userId()).isEqualTo(JAVAJIGI.userId())
		);
	}

	@Test
	public void findByUserId () {
		users.save(JAVAJIGI);

		final Optional<User> byUserId = users.findByUserId(JAVAJIGI.userId());

		assertThat(byUserId).isNotEmpty();
	}
}
