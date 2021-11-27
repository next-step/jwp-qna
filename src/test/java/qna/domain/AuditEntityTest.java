package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AuditEntityTest {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Test
	void JPAaudit_working() {
		User user = new User("userId", "userPasswd", "userName", "userEmail");
		Question question = new Question("questionTitle", "questionContents");
		Answer answer = new Answer(user, question, "answerContents");
		userRepository.save(user);
		questionRepository.save(question);
		answerRepository.save(answer);

		assertThat(user.createdAt).isNotNull();
		assertThat(question.createdAt).isNotNull();
		assertThat(answer.createdAt).isNotNull();
	}
}
