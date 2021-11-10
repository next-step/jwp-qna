package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	void save() {
		final User expected = new User("userId", "password", "name", "e@mail.com");
		final User actual = userRepository.save(expected);
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId()),
			() -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
			() -> assertThat(actual.getName()).isEqualTo(expected.getName()),
			() -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
		);
		assertThat(actual).isEqualTo(actual);
	}

	@Test
	void findByUserId() {
		final User expected = userRepository.save(new User("userId", "password", "name", "e@mail.com"));
		final Optional<User> actual = userRepository.findByUserId(expected.getUserId());
		assertThat(actual.isPresent()).isTrue();
		assertThat(actual.get()).isEqualTo(expected);
	}
}
