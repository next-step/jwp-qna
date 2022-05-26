package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {
	private User javajigi;

	@Autowired
	UserRepository userRepository;

	@BeforeEach
	void init() {
		javajigi = UserTest.JAVAJIGI;
	}

	@Test
	@DisplayName("User 생성")
	void save() {
		User actual = userRepository.save(javajigi);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getUserId()).isEqualTo(javajigi.getUserId())
		);
	}

	@Test
	@DisplayName("User Id로 조회")
	void findByUserId() {
		User expected = userRepository.save(javajigi);
		Optional<User> actual = userRepository.findByUserId(expected.getUserId());
		
		assertThat(actual.isPresent()).isTrue();
	}
}
