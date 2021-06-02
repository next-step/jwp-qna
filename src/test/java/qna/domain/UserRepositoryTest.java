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

	User savedUser;

	@BeforeEach
	void setUp() {
		savedUser = new User("wrallee", "password", "우찬", "wrallee@gmail.com");
		users.save(savedUser);
	}

	@Test
	void save() {
		User expected = new User("wrallee", "password", "우찬", "wrallee@gmail.com");
		User actual = users.save(expected);
		assertSame(expected, actual);
		assertNotNull(actual.getCreatedAt());
		assertNotNull(actual.getUpdatedAt());
	}

	@Test
	void findByUserId() {
		User actual = users.findByUserId(savedUser.getName()).orElse(null);
		assertNotNull(actual);
		assertSame(savedUser, actual);
	}
}