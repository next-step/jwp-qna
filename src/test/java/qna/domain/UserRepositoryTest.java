package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {
	@Autowired
	UserRepository userRepository;

	@Test
	@DisplayName("User 생성")
	void save() {
		User expected = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		User actual = userRepository.save(expected);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId())
		);
	}

	@Test
	@DisplayName("User Id로 조회")
	void findByUserId() {
		User expected = userRepository.save(new User(1L, "javajigi", "password", "name", "javajigi@slipp.net"));
		Optional<User> actual = userRepository.findByUserId(expected.getUserId());
		
		assertThat(actual.isPresent()).isTrue();
	}
}
