package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
public class UserTest {

	public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
	public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

	@Autowired
	UserRepository userRepository;

	@Test
	void inputNullUserId() {
		// given
		User user = new User(null, "test", "password", "name");

		// when // then
		assertThatThrownBy(() -> {
			userRepository.save(user);
		}).isInstanceOf(DataIntegrityViolationException.class)
			.hasMessageContaining("null");
	}

	@Test
	void inputEmailOverLengthRange() {
		// given
		String email = "12345678910123456789qweqw4e56qw4e56wq45e4wq56e45wq64e65wq45e6wq121233";
		SANJIGI.setEmail(email);

		// when // then
		assertThatThrownBy(() -> {
			userRepository.save(SANJIGI);
		}).isInstanceOf(DataIntegrityViolationException.class);
	}

	@Test
	void findById() {
		// given
		User user = new User("id", "test", "password", "name");
		User expectUser = userRepository.save(user);

		// when
		Optional<User> userOptional = userRepository.findById(expectUser.getId());

		// then
		assertThat(userOptional.isPresent()).isTrue();
	}
}
