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

	private User user;
	private DeleteHistory deleteHistory;
	private Answer answer;
	private Question question;

	@BeforeEach
	void setup() {
		User expectedUser = new User("testUser", "testPassword", "testName", "testEmail");
		this.deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, user, null);
		expectedUser.addDeleteHistory(this.deleteHistory);
		this.question = new Question("questionTitle", "questionContents");
		expectedUser.addQuestion(this.question);
		this.answer = new Answer(expectedUser, this.question, "answerContents");
		expectedUser.addAnswer(this.answer);

		this.user = this.userRepository.save(expectedUser);
	}

	@Test
	@DisplayName("user save 테스트")
	void test_save() {
		assertThat(this.user.getId()).isNotNull();
	}

	@Test
	@DisplayName("userID로 user를 찾아 반환값 테스트")
	void testFindById() {
		User user = this.userRepository.findByUserId(this.user.getUserId())
			.orElseThrow(() -> new EntityNotFoundException("아이디에 해당하는 User를 찾을 수 없습니다."));
		this.isEqualTo(this.user, user);
	}

	@Test
	@DisplayName("user를 제거하고 find시 빈값 테스트")
	void testDeleteAndFindById() {
		this.userRepository.delete(this.user);
		Optional<User> userOpt = this.userRepository.findByUserId(this.user.getUserId());
		assertThat(userOpt.isPresent()).isFalse();
	}

	@Test
	@DisplayName("user의 deleteHistory 목록을 반환")
	void test_userDeleteHistory() {
		assertThat(this.user.getDeleteHistories()).contains(this.deleteHistory);
	}

	@Test
	@DisplayName("user의 question 목록을 반환")
	void test_userQuestions() {
		assertThat(this.user.getQuestions()).contains(this.question);
	}

	@Test
	@DisplayName("user의 answer 목록을 반환")
	void test_userAnswers() {
		assertThat(this.user.getAnswers()).contains(this.answer);
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