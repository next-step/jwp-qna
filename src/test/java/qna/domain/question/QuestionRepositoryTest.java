package qna.domain.question;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.question.QuestionTest.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import qna.domain.user.UserTest;

@DataJpaTest
@DisplayName("Question Repository 테스트")
class QuestionRepositoryTest {

	@Autowired
	private QuestionRepository questionRepository;

	@BeforeEach
	void setUp() {
		questionRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("질문 저장 테스트")
	void saveTest() {
		Question question = questionRepository.save(Q1);

		assertAll(
			() -> assertThat(question).isNotNull(),
			() -> assertThat(question.getId()).isNotNull(),
			() -> assertThat(question.getId()).isEqualTo(Q1.getId()),
			() -> assertThat(question.getTitle()).isEqualTo(Q1.getTitle()),
			() -> assertThat(question.getContents()).isEqualTo(Q1.getContents()),
			() -> assertThat(question.isDeleted()).isEqualTo(Q1.isDeleted()),
			() -> assertThat(question.getWriter()).isEqualTo(Q1.getWriter())
		);
	}

	@Test
	@DisplayName("질문 저장 후 조회 테스트")
	void findByIdTest() {
		Question question = questionRepository.save(Q1);

		Question actual = questionRepository.findById(question.getId())
			.orElseThrow(() -> new IllegalArgumentException("entity is not found"));

		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getTitle()).isEqualTo(question.getTitle()),
			() -> assertThat(actual.getContents()).isEqualTo(question.getContents()),
			() -> assertThat(actual.isDeleted()).isEqualTo(question.isDeleted()),
			() -> assertThat(actual.getWriter()).isEqualTo(question.getWriter())
		);
	}

	@Test
	@DisplayName("질문 리스트 조회 테스트")
	void findAllTest() {
		Question question1 = questionRepository.save(Q1);
		Question question2 = questionRepository.save(Q2);

		List<Question> questions = questionRepository.findByDeletedFalse();

		assertAll(
			() -> assertThat(questions).isNotNull(),
			() -> assertThat(questions).hasSize(2),
			() -> assertThat(questions).containsExactly(question1, question2)
		);
	}

	@Test
	@DisplayName("100 자가 넘는 title 저장 시 예외 발생 테스트")
	void invalidTitleMaxLengthTest() {
		// given
		StringBuilder title = new StringBuilder("a");
		for (int i = 0; i < 100; i++) {
			title.append("a");
		}
		Question questions = new Question(title.toString(), "contents1").writeBy(UserTest.JAVAJIGI);

		// when, then
		assertThatThrownBy(() -> questionRepository.save(questions))
			.isInstanceOf(DataIntegrityViolationException.class);
	}

	@Test
	@DisplayName("삭제 여부 true 로 변경 후 조회 테스트")
	void deleteTest() {
		// given
		Question question = questionRepository.save(Q1);
		Long id = question.getId();

		// when
		question.setDeleted(true);
		Optional<Question> byIdAndDeletedFalse = questionRepository.findByIdAndDeletedFalse(id);

		// then
		assertAll(
			() -> assertThat(byIdAndDeletedFalse).isEmpty(),
			() -> assertThat(question.isDeleted()).isTrue(),
			() -> assertThat(questionRepository.findById(id)).isNotEmpty(),
			() -> assertThat(questionRepository.findByIdAndDeletedFalse(id)).isEmpty()
		);
	}

	@Test
	@DisplayName("질문 저장 후 삭제 테스트")
	void deleteByIdTest() {
		// given
		Question question = questionRepository.save(Q1);
		Long id = question.getId();

		// when
		questionRepository.deleteById(id);

		// then
		assertAll(
			() -> assertThat(questionRepository.findById(id)).isEmpty(),
			() -> assertThat(questionRepository.findByIdAndDeletedFalse(id)).isEmpty()
		);
	}

}
