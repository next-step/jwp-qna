package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
	@Autowired
	private UserRepository userRepository;

	public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
	public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

	@Test
	@DisplayName("jpql 사용)")
	void select_name_by_email_using_jpql() {
		User saveA1 = userRepository.save(JAVAJIGI);
		User saveA2 = userRepository.save(SANJIGI);

		userRepository.findByEmail("javajigi");

		assertThat((String)userRepository.findByEmail("javajigi").get(0)[0]).isEqualTo("javajigi");
	}

	@Test
	@DisplayName("jpa 작성 메소드 사용(findByUserId)")
	void use_written_method_findByUserId() {
		User saveA1 = userRepository.save(JAVAJIGI);
		User saveA2 = userRepository.save(SANJIGI);

		assertThat(userRepository.findByUserId(saveA1.getUserId()).get()).isEqualTo(
			userRepository.findById(saveA1.getId()).get());
		assertThat(userRepository.findByUserId(saveA2.getUserId()).get()).isEqualTo(
			userRepository.findById(saveA2.getId()).get());
	}
}
