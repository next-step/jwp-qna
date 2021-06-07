package qna.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnswerRepositoryTest {

	@Autowired
	EntityManager em;

	@Autowired
	UserRepository users;

	@Autowired
	QuestionRepository questions;

	@Autowired
	AnswerRepository answers;

	User savedUser;

	Question savedQuestion;

	Answer savedAnswer;

	Answer deletedAnswer;

	@BeforeEach
	void setUp() {
		User user = new User("wrallee", "password", "우찬", "wrallee@gmail.com");
		Question question = new Question("my title", "my contents").writeBy(user);
		savedUser = users.save(user);
		savedQuestion = questions.save(question);

		savedAnswer = new Answer(savedUser, savedQuestion, "this is saved Answer");
		deletedAnswer = new Answer(savedUser, savedQuestion, "this is deleted Answer");

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
		List<Answer> actual = answers.findByQuestionIdAndDeletedFalse(savedQuestion.getId());
		assertEquals(actual.size(), 1);
		assertSame(actual.get(0), savedAnswer);
	}

	@Test
	void findAnswersFromQuestion() {
		Question question = questions.findById(savedQuestion.getId()).orElse(null);
		List<Answer> answers = question.getAnswers();
		assertEquals(answers.size(), 2);
		assertSame(savedAnswer, answers.get(0));
		assertSame(deletedAnswer, answers.get(1));
	}
}
