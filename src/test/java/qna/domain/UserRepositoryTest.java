package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.fixture.UserFixture;

@DisplayName("사용자 저장소")
@DataJpaTest
class UserRepositoryTest {
	@Autowired
	private UserRepository userRepository;

	@DisplayName("사용자를 저장할 수 있다.")
	@Test
	void save() {
		// given
		User expected = UserFixture.Y2O2U2N();

		// when
		User actual = userRepository.save(expected);

		// then
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail()),
			() -> assertThat(actual.getName()).isEqualTo(expected.getName()),
			() -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
			() -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId())
		);
	}

	@DisplayName("사용자 ID로 사용자를 찾을 수 있다.")
	@Test
	void findByUserId() {
		// given
		User expected = userRepository.save(UserFixture.SEMISTONE222());

		// when
		User actual = userRepository.findByUserId(expected.getUserId())
			.orElseThrow(AssertionFailedError::new);

		// then
		assertThat(actual).isEqualTo(expected);
	}
}
