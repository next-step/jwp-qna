package qna.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.domain.User;

@DataJpaTest
public class UserRepositoryTest {
	@Autowired
	UserRepository users;

	@Test
	void save() {
		User expected = new User("javajigi", "password", "name", "javajigi@slipp.net");
		User actual = users.save(expected);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
			() -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
		);
	}

	@Test
	void findByUserId() {
		User expected = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		users.save(expected);
		User actual = users.findByUserId("javajigi").orElse(null);

		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
			() -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
		);
	}
}