package qna.domain.user;

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
		User user = repository.findByUserId(new UserId(save.getUserId())).get();
		Assertions.assertThat(save).isEqualTo(user);
	}
}
