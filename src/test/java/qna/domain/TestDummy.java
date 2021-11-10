package qna.domain;

public class TestDummy {

	public static final User USER_JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
	public static final User USER_SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
	public static final Question QUESTION1 = new Question("title1", "contents1").writeBy(USER_JAVAJIGI);
	public static final Question QUESTION2 = new Question("title2", "contents2").writeBy(USER_SANJIGI);
	public static final Answer ANSWER1 = new Answer(USER_JAVAJIGI, QUESTION1, "Answers Contents1");
	public static final Answer ANSWER2 = new Answer(USER_SANJIGI, QUESTION1, "Answers Contents2");
}
