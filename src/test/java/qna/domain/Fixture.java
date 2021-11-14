package qna.domain;

public class Fixture {

	public static User user(String userId) {
		return new User(userId, "password", "name", "e@mail.com");
	}

	public static Question question(String writerId) {
		final Question question = new Question("title", "question.contents");
		question.writeBy(user(writerId));
		return question;
	}

	public static Answer answer(String writerId) {
		final Question question = question(String.format("question.%s", writerId));
		return new Answer(user(writerId), question, "answer.contents");
	}
}
