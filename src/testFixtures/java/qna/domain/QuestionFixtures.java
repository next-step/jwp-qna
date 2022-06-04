package qna.domain;

public class QuestionFixtures {
    public static Question Q1 = new Question("title1", "contents1").writeBy(UserFixtures.JAVAJIGI);
    public static Question Q2 = new Question("title2", "contents2").writeBy(UserFixtures.SANJIGI);

    public static Question create(String title, String content) {
        return new Question(title, content);
    }

    public static Question createDefaultByUser(User user) {
        return new Question("title", "content").writeBy(user);
    }

    public static Question createByUser(String title, String content, User user) {
        return new Question(title, content).writeBy(user);
    }
}
