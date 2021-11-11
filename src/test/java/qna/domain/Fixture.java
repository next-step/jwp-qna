package qna.domain;

public class Fixture {

	public static User user(String userId) {
		return new User(userId, "password", "name", "e@mail.com");
	}

	public static Question question(String writerId) {
		final User writer = user(writerId);
		final Question question = new Question("title", "question.contents");
		question.writeBy(writer);
		return question;
	}

	public static Answer answer(String writerId) {
		final User writer = user(writerId);
		final Question question = question("question.writer.id");
		return new Answer(writer, question, "answer.contents");
	}
}
