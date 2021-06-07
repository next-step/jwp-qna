package qna.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.domain.User;
import qna.domain.UserTest;
import qna.domain.exception.question.AnswerOwnerNotMatchedException;
import qna.domain.question.Answer;
import qna.domain.question.Question;

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
	void before() throws AnswerOwnerNotMatchedException {
		User javaJigi = userRepository.save(UserTest.JAVAJIGI);
		question = questionRepository.save(new Question(
			"question title", "question contents"
		));
		answer = repository.save(new Answer(javaJigi, this.question, notDeletedAnswerContents));
		deletedAnswer = new Answer(javaJigi, question, "deleted answer contents");
		deletedAnswer.deleteBy(javaJigi);
		deletedAnswer = repository.save(deletedAnswer);
	}

	@Test
	@DisplayName("삭제되지 않은 Answer만 조회되는지 검증")
	void findByQuestionIdAndDeletedFalse() {
		List<Answer> notDeletedAnswers = repository.findByQuestionIdAndDeletedFalse(question.getId());
		assertThat(notDeletedAnswers)
			.hasSize(1)
			.first().hasFieldOrPropertyWithValue("contents", notDeletedAnswerContents)
		;
	}
}