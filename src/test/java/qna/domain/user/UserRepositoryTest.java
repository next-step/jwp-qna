package qna.domain.user;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.user.UserTest.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("User Repository 테스트")
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		userRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("회원 저장 테스트")
	void saveTest() {
		User user = userRepository.save(JAVAJIGI);

		assertAll(
			() -> assertThat(user).isNotNull(),
			() -> assertThat(user.getId()).isNotNull(),
			() -> assertThat(user.getId()).isEqualTo(JAVAJIGI.getId()),
			() -> assertThat(user.getUserId()).isEqualTo(JAVAJIGI.getUserId()),
			() -> assertThat(user.getPassword()).isEqualTo(JAVAJIGI.getPassword()),
			() -> assertThat(user.getName()).isEqualTo(JAVAJIGI.getName()),
			() -> assertThat(user.getEmail()).isEqualTo(JAVAJIGI.getEmail())
		);
	}

	@Test
	@DisplayName("회원 저장 후 조회 테스트 - id")
	void findByIdTest() {
		User user = userRepository.save(JAVAJIGI);

		User actual = userRepository.findById(user.getId())
			.orElseThrow(() -> new IllegalArgumentException("entity is not found"));

		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getUserId()).isEqualTo(user.getUserId()),
			() -> assertThat(actual.getPassword()).isEqualTo(user.getPassword()),
			() -> assertThat(actual.getName()).isEqualTo(user.getName()),
			() -> assertThat(actual.getEmail()).isEqualTo(user.getEmail())
		);
	}

	@Test
	@DisplayName("회원 저장 후 조회 테스트 - userId")
	void findByUserIdTest() {
		User user = userRepository.save(JAVAJIGI);

		User actual = userRepository.findByUserId(user.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("entity is not found"));

		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getUserId()).isEqualTo(user.getUserId()),
			() -> assertThat(actual.getPassword()).isEqualTo(user.getPassword()),
			() -> assertThat(actual.getName()).isEqualTo(user.getName()),
			() -> assertThat(actual.getEmail()).isEqualTo(user.getEmail())
		);
	}



}