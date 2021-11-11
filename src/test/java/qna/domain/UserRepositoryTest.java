package qna.domain;

import static qna.domain.UserTest.*;

import java.util.Arrays;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		userRepository.saveAll(Arrays.asList(JAVAJIGI, SANJIGI));
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
}
