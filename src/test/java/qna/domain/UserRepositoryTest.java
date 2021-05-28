package qna.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

	@Autowired
	private UserRepository repository;

	@Test
	void saveAndFind() {
		User save = repository.save(UserTest.JAVAJIGI);
		User user = repository.findByUserId(save.getUserId()).get();
		Assertions.assertThat(save).isEqualTo(user);
	}
}
