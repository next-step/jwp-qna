package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserTest {
	public static final User JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
	public static final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");

	private final UserRepository userRepository;

	@Autowired
	public UserTest(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Test
	void save_test() {
		User actual = userRepository.save(JAVAJIGI);
		assertThat(actual.getId()).isNotNull();
	}

	@Test
	void findByUserId_test() {
		userRepository.save(JAVAJIGI);
		User actual = userRepository.findByUserId("javajigi").get();
		assertThat(actual.getUserId()).isEqualTo("javajigi");
	}
}
