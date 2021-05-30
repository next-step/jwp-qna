package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
@DataJpaTest
class AnswerRepositoryTest {

	@Autowired
	private AnswerRepository repository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private QuestionRepository questionRepository;

	private Answer answer;
	private User savedUser;
	private Question savedQuestion;

	@BeforeEach
	void setUp() {
		savedUser = userRepository.save(new User("len", "password", "name", "email"));

		Question question = new Question("title", "Contents");
		savedQuestion = questionRepository.save(question);

		answer = new Answer(savedUser, savedQuestion, "Answer Contents");
	}

	@Test
	void findByIdAndDeletedFalseTest() {


		Answer save = repository.save(answer);

		Answer answer1 = repository.findByIdAndDeletedFalse(save.getId()).orElse(Answer.NONE);
		assertThat(answer1).isEqualTo(save);

		save.setDeleted(true);

		Answer answer2 = repository.findByIdAndDeletedFalse(save.getId()).orElse(Answer.NONE);
		assertThat(answer2).isEqualTo(Answer.NONE);
	}

	@Test
	void findByQuestionIdAndDeletedFalseTest() {

		Answer answer = new Answer(savedUser, savedQuestion, "Answer Contents");
		Answer answer2 = new Answer(savedUser, savedQuestion, "another Contents");

		repository.save(answer);
		repository.save(answer2);

		List<Answer> answers = repository.findByQuestionIdAndDeletedFalse(savedQuestion.getId());
		assertThat(answers).containsExactly(answer, answer2);
	}
}
