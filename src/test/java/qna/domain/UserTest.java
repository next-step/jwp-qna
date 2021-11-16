package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
	public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
	public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
	@Autowired
	private UserRepository userRepository;

	@Test
	void save() {
		User actual = userRepository.save(JAVAJIGI);
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getName()).isEqualTo(JAVAJIGI.getName())
		);
	}

	@Test
	void findByName() {
		userRepository.save(JAVAJIGI);
		final List<User> actual = userRepository.findByName("name");

		assertThat(actual.get(0)).isEqualTo(JAVAJIGI);
	}
}
