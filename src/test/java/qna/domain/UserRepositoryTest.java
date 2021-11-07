package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.fixture.UserFixture;

@DataJpaTest
class UserRepositoryTest {
	@Autowired
	private UserRepository users;

	@Test
	void save() {
		// given
		User expected = UserFixture.Y2O2U2N();

		// when
		User actual = users.save(expected);

		// then
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail()),
			() -> assertThat(actual.getName()).isEqualTo(expected.getName()),
			() -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
			() -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId())
		);
	}

	@Test
	void findByUserId() {
		// given
		User expected = UserFixture.SEMISTONE222();
		users.save(expected);

		// when
		User actual = users.findByUserId(expected.getUserId())
			.orElseThrow(RuntimeException::new);

		// then
		assertThat(actual).isEqualTo(expected);
	}
}
