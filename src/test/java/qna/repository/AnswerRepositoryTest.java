package qna.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
class AnswerRepositoryTest {

	@Autowired
	UserRepository userRepository;

	@Autowired
	AnswerRepository repository;

	@Autowired
	QuestionRepository questionRepository;

	Question question;

	Answer deletedAnswer;

	Answer answer;

	String notDeletedAnswerContents = "answer contents";

	@BeforeEach
	void before() {
		User javaJigi = userRepository.save(UserTest.JAVAJIGI);
		question = questionRepository.save(new Question(
			"question title", "question contents"
		));
		answer = repository.save(new Answer(javaJigi, this.question, notDeletedAnswerContents));
		deletedAnswer = new Answer(javaJigi, question, "deleted answer contents");
		deletedAnswer.delete();
		deletedAnswer = repository.save(deletedAnswer);
	}

	@Test
	void findByQuestionIdAndDeletedFalse() {
		List<Answer> notDeletedAnswers = repository.findByQuestionIdAndDeletedFalse(question.getId());
		assertThat(notDeletedAnswers)
			.hasSize(1)
			.first().hasFieldOrPropertyWithValue("contents", notDeletedAnswerContents)
		;
	}
}