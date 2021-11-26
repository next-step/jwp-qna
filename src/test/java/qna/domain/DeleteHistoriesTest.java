package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoriesTest {
	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private UserRepository userRepository;

	@Test
	void addQuestion() {
		User user = userRepository.save(UserTest.JAVAJIGI);
		Question q1 = questionRepository.save(new Question("questionTitle", "questionContents").writeBy(user));
		DeleteHistories deleteHistories = new DeleteHistories();
		deleteHistories.add(q1, LocalDateTime.now());

		assertThat(deleteHistories.get().size()).isEqualTo(1);
	}

	@Test
	void addAnswers() {
		User user = new User("userId", "userPasword", "userName", "userEmail");
		Question q1 = new Question("questionTitle", "questionContents");
		Answer a1 = new Answer(user, q1, "answerContents");
		Answers answers = new Answers(Collections.singletonList(a1));
		DeleteHistories deleteHistories = new DeleteHistories();
		deleteHistories.add(answers, LocalDateTime.now());

		assertThat(deleteHistories.get().size()).isEqualTo(1);
	}
}
