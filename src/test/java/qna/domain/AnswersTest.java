package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class AnswersTest {
	private static final User user1 = new User(1L, "userId", "password", "name", "email");
	private static final User user2 = new User(2L, "userId2", "password2", "name2", "email2");
	private static final Question question = new Question("questionTitle", "questionContents");

	@Test
	void haveNotOwner() {
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

	@Test
	void setDeleted() {
		Answer answer1 = new Answer(user1, question, "answerContents");
		answer1.setDeleted(true);
		Answer answer2 = new Answer(user2, question, "answerContents2");
		answer2.setDeleted(true);

		Answer answer3 = new Answer(user1, question, "answerContents");
		Answer answer4 = new Answer(user2, question, "answerContents2");
		Answers answers = new Answers(
			Arrays.asList(answer3, answer4)
		);
		answers.setDeleted(true);

		assertThat(answers).isEqualTo(new Answers(Arrays.asList(answer1, answer2)));
	}
}
