package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.fixture.UserFixture;

@DataJpaTest
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("저장하기 전후의 객체가 서로 동일한 객체인가")
	void save() {
		final User expected = UserFixture.식별자가_userId인_유저("user.id");
		final User actual = userRepository.save(expected);
		assertAll(
			() -> assertThat(actual).isSameAs(expected),
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId()),
			() -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
			() -> assertThat(actual.getName()).isEqualTo(expected.getName()),
			() -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
		);
	}

	@Test
	@DisplayName("저장된 객체가 문자열 userId로 검색한 객체와 동일한가")
	void findByUserId() {
		final User expected = userRepository.save(UserFixture.식별자가_userId인_유저("user.id"));
		final Optional<User> actual = userRepository.findByUserId(expected.getUserId());
		assertThat(actual.isPresent()).isTrue();
		assertThat(actual.get()).isSameAs(expected);
	}
}
