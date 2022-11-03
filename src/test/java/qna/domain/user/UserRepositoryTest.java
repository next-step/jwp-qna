package qna.domain.user;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.user.UserTest.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;

import qna.domain.generator.UserGenerator;

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Import(UserGenerator.class)
@DisplayName("User Repository 테스트")
class UserRepositoryTest {

	private final UserRepository userRepository;
	private final UserGenerator userGenerator;
	private final EntityManager entityManager;

	public UserRepositoryTest(UserRepository userRepository, UserGenerator userGenerator, EntityManager entityManager) {
		this.userRepository = userRepository;
		this.userGenerator = userGenerator;
		this.entityManager = entityManager;
	}

	@Test
	@DisplayName("회원 저장 테스트")
	void saveTest() {
		User user = userRepository.save(UserGenerator.questionWriter());

		assertAll(
			() -> assertThat(user).isNotNull(),
			() -> assertThat(user.getId()).isNotNull()
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
			() -> assertThat(actual.getEmail()).isEqualTo(user.getEmail()),
			() -> assertThat(actual).isSameAs(user)
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
			() -> assertThat(actual.getEmail()).isEqualTo(user.getEmail()),
			() -> assertThat(actual).isSameAs(user)
		);
	}

	@Test
	@DisplayName("회원 정보 수정 테스트 - dirty check 이용")
	void updateUserTest() {
		User user = userGenerator.savedUser();
		String newName = "newName";
		String newEmail = "newEmail";
		User target = new User(user.getId(), user.getUserId(), user.getPassword(), newName, newEmail);

		user.update(user, target);
		entityManager.flush();

		assertAll(
			() -> assertThat(user.getName()).isEqualTo(newName),
			() -> assertThat(user.getEmail()).isEqualTo(newEmail)
		);
	}
}