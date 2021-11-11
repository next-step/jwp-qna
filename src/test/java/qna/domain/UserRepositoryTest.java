package qna.domain;

import static qna.domain.UserTest.*;

import java.util.Arrays;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		/*userRepository.saveAll(Arrays.asList(JAVAJIGI, SANJIGI));*/
		userRepository.save(JAVAJIGI);
	}

	@Test
	void updateEmail() {
		// given
		Optional<User> byUserId = userRepository.findByUserId(JAVAJIGI.getUserId());
		User user = byUserId.get();

		// when
		final String updateEmail = "seunghoo@naver.com";
		user.setEmail(updateEmail);

		// then
		Assertions.assertThat(user.getEmail()).isEqualTo(updateEmail);
	}

	@Test
	void findByName() {
		// when
		User user = userRepository.findByName("javajigi").get();

		// then
		Assertions.assertThat(user.getName()).isEqualTo(JAVAJIGI.getName());
	}
}
