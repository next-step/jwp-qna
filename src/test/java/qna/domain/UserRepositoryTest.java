package qna.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

	@Autowired
	UserRepository users;

	User expected;

	@BeforeEach
	void setUp() {
		expected = new User(
			"wrallee",
			"password",
			"우찬",
			"wrallee@gmail.com");
	}

	@Test
	void save() {
		User actual = users.save(expected);
		assertSame(expected, actual);
		assertNotNull(actual.getCreatedAt());
		assertNotNull(actual.getUpdatedAt());
	}

	@Test
	void findByUserId() {
		users.save(expected);
		User actual = users.findByUserId("wrallee").orElse(null);
		assertNotNull(actual);
		assertSame(expected, actual);
	}
}