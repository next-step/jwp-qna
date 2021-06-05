package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QuestionRepositoryTest {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private UserRepository userRepository;

	private Question question;
	private Answer answer;

	@BeforeEach
	void setup() {
		User user = new User("testUser", "testPassword", "testName", "testEmail");
		User savedUser = this.userRepository.save(user);
		Question question = new Question("questionTitle", "questionContents");
		question.writeBy(savedUser);

		this.answer = new Answer(savedUser, question, "answerContents");
		question.addAnswer(this.answer);

		this.question = this.questionRepository.save(question);
	}

	@Test
	@DisplayName("question entity 저장 확인")
	void test_save() {
		assertThat(this.question.getId()).isNotNull();
	}

	@Test
	@DisplayName("question id가 존재하면 questionEntity반환")
	void test_findByIdAndDeletedFalse() {
		Optional<Question> actualOpt = this.questionRepository.findByIdAndDeletedFalse(this.question.getId());
		Question actual = actualOpt.orElseThrow(() -> new EntityNotFoundException("id에 해당하는 Question 을 찾을 수 없습니다."));
		this.isEqualTo(this.question, actual);
	}

	@Test
	@DisplayName("question id가 없는경우")
	void test_findByIdAndDeletedTrue() {
		this.questionRepository.delete(this.question);
		Optional<Question> questionOpt = this.questionRepository.findByIdAndDeletedFalse(this.question.getId());

		assertThat(questionOpt.isPresent()).isFalse();
	}

	@Test
	@DisplayName("question 목록이 반환 테스트")
	void test_findByDeletedFalse() {
		List<Question> actual = this.questionRepository.findByDeletedFalse();
		assertThat(actual).contains(this.question);
	}

	@Test
	@DisplayName("question 목록이 없는경우")
	void test_findByDeletedTrue() {
		this.questionRepository.delete(this.question);
		List<Question> actual = this.questionRepository.findByDeletedFalse();
		assertThat(actual).isEmpty();
	}

	@Test
	@DisplayName("question의 answer 목록 반환 테스트")
	void test_questionAnswers() {
		assertThat(this.question.isAnswerContains(this.answer)).isTrue();
	}

	private void isEqualTo(Question expected, Question actual) {
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
			() -> assertThat(actual.getWriter()).isEqualTo(expected.getWriter()),
			() -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
			() -> assertThat(actual.getCreatedAt()).isNotNull(),
			() -> assertThat(actual.getUpdatedAt()).isNotNull()
		);
	}
}