package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	private User expected;
	private User saved;

	@BeforeEach
	void setup() {
		this.expected = UserTest.JAVAJIGI;
		this.saved = this.userRepository.save(expected);
	}

	@Test
	@DisplayName("user save 테스트")
	void test_save() {
		this.isEqualTo(expected, saved);
	}

	@Test
	@DisplayName("userID로 user를 찾아 반환값 테스트")
	void testFindById() {
		User user = this.userRepository.findByUserId(saved.getUserId())
			.orElseThrow(() -> new EntityNotFoundException("아이디에 해당하는 User를 찾을 수 없습니다."));
		this.isEqualTo(saved, user);
	}

	@Test
	@DisplayName("user를 제거하고 find시 빈값 테스트")
	void testDeleteAndFindById() {
		this.userRepository.delete(saved);
		Optional<User> userOpt = this.userRepository.findByUserId(saved.getUserId());
		assertThat(userOpt.isPresent()).isFalse();
	}

	private void isEqualTo(User expected, User actual) {
		Assertions.assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail()),
			() -> assertThat(actual.getName()).isEqualTo(expected.getName()),
			() -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
			() -> assertThat(actual.getCreatedAt()).isNotNull(),
			() -> assertThat(actual.getUpdatedAt()).isNotNull(),
			() -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId())
		);
	}
}