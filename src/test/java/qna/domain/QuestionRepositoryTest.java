package qna.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {

	@Autowired
	QuestionRepository questions;

	Question savedQuestion;

	Question deletedQuestion;

	@BeforeEach
	void setUp() {
		savedQuestion = new Question("my title", "my contents");
		deletedQuestion = new Question("deleted title", "deleted contents");

		deletedQuestion.setDeleted(true);

		questions.save(savedQuestion);
		questions.save(deletedQuestion);
	}

	@Test
	void save() {
		Question expected = new Question("my title", "my contents");
		Question actual = questions.save(expected);
		assertSame(expected, actual);
	}

	@Test
	void findByDeletedFalse() {
		List<Question> questionList = questions.findByDeletedFalse();
		assertEquals(questionList.size(), 1);
		assertSame(savedQuestion, questionList.get(0));
	}

	@Test
	void findByIdAndDeletedFalse() {
		Question deleted = questions.findByIdAndDeletedFalse(deletedQuestion.getId()).orElse(null);
		Question notDeleted = questions.findByIdAndDeletedFalse(savedQuestion.getId()).orElse(null);
		assertNull(deleted);
		assertNotNull(notDeleted);
	}
}