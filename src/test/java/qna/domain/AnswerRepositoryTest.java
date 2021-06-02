package qna.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnswerRepositoryTest {
	@Autowired
	AnswerRepository answers;

	Answer savedAnswer;

	Answer deletedAnswer;

	@BeforeEach
	void setUp() {
		User mockUser = new User(10L, "wrallee", "password", "우찬", "wrallee@gmail.com");
		Question mockQuestion = new Question(20L, "my title", "my contents");
		savedAnswer = new Answer(mockUser, mockQuestion, "this is saved Answer");
		deletedAnswer = new Answer(mockUser, mockQuestion, "this is deleted Answer");

		deletedAnswer.setDeleted(true);

		answers.save(savedAnswer);
		answers.save(deletedAnswer);
	}

	@Test
	void findByIdAndDeletedFalse() {
		Answer actualExist = answers.findByIdAndDeletedFalse(savedAnswer.getId()).orElse(null);
		Answer actualNotExist = answers.findByIdAndDeletedFalse(deletedAnswer.getId()).orElse(null);
		assertNotNull(actualExist);
		assertNull(actualNotExist);
	}

	@Test
	void findByQuestionIdAndDeletedFalse() {
		Long questionId = 20L;
		List<Answer> actual = answers.findByQuestionIdAndDeletedFalse(questionId);
		assertEquals(actual.size(), 1);
		assertSame(actual.get(0), savedAnswer);
	}
}
