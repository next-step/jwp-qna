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
		User actual = userRepository.save(UserTest.JAVAJIGI);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getUserId()).isEqualTo(UserTest.JAVAJIGI.getUserId())
		);
	}

	@Test
	@DisplayName("User Id로 조회")
	void findByUserId() {
		User expected = userRepository.save(UserTest.JAVAJIGI);
		Optional<User> actual = userRepository.findByUserId(expected.getUserId());
		
		assertThat(actual.isPresent()).isTrue();
	}
}
