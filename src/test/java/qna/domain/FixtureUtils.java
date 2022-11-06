package qna.domain;

public class FixtureUtils {
    public static User JAVAJIGI() {
        return new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    }

    public static User SANJIGI() {
        return new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
    }

    public static Question Q1() {
        return new Question(1L, "title1", "contents1").writeBy(JAVAJIGI());
    }

    public static Question Q1(User user) {
        return new Question(1L, "title1", "contents1").writeBy(user);
    }

    public static Question Q2() {
        return new Question(2L, "title2", "contents2").writeBy(JAVAJIGI());
    }

    public static Answer A1() {
        return new Answer(1L, JAVAJIGI(), Q1(), "Answers Contents1");
    }

    public static Answer A1(User user, Question question) {
        return new Answer(1L, user, question, "Answers Contents1");
    }

    public static Answer A2() {
        return new Answer(2L, JAVAJIGI(), Q1(), "Answers Contents2");
    }
}
