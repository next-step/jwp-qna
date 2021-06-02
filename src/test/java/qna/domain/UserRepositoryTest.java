package qna.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

	@Autowired
	UserRepository users;

	@Test
	void jpaAuditingTest() {
		User expected = new User("wrallee", "password", "우찬", "abc@gmail.com");
		User actual = users.save(expected);
		assertNotNull(actual.getCreatedAt());
		assertNotNull(actual.getUpdatedAt());
	}
}