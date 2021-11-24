package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class AnswersTest {
	@Test
	void haveNotOwner() {
		User user1 = new User(1L,"userId", "password", "name", "email");
		User user2 = new User(2L,"userId2", "password2", "name2", "email2");
		Question question = new Question("questionTitle", "questionContents");
		Answers answers1 = new Answers(
			Arrays.asList(
				new Answer(user1, question, "answerContents"),
				new Answer(user1, question, "answerContents2")
			)
		);
		Answers answers2 = new Answers(
			Arrays.asList(
				new Answer(user1, question, "answerContents"),
				new Answer(user2, question, "answerContents2")
			)
		);

		assertThat(answers1.haveNotOwner(user1)).isFalse();
		assertThat(answers2.haveNotOwner(user1)).isTrue();
	}
}
