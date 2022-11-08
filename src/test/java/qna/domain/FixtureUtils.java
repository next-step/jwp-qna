package qna.domain;

public class FixtureUtils {
    public static User JAVAJIGI() {
        return new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    }

    public static User SANJIGI() {
        return new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
    }

    public static Question QUESTION1_WRITE_BY_JAVAJIGI() {
        return new Question(1L, "title1", "contents1").writeBy(JAVAJIGI());
    }

    public static Question QUESTION1(User user) {
        return new Question(1L, "title1", "contents1").writeBy(user);
    }

    public static Question QUESTION2_WRITE_BY_JAVAJIGI() {
        return new Question(2L, "title2", "contents2").writeBy(JAVAJIGI());
    }

    public static Answer ANSWER1_WRITE_BY_JAVAJIGI() {
        return new Answer(1L, JAVAJIGI(), QUESTION1_WRITE_BY_JAVAJIGI(), "Answers Contents1");
    }

    public static Answer ANSWER1(User user, Question question) {
        return new Answer(1L, user, question, "Answers Contents1");
    }

    public static Answer ANSWER2_WRITE_BY_JAVAJIGI() {
        return new Answer(2L, JAVAJIGI(), QUESTION1_WRITE_BY_JAVAJIGI(), "Answers Contents2");
    }
}
