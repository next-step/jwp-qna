package qna.domain;

public class TestQuestionFactory {
    public static Question create() {
        User user = TestUserFactory.create();
        return new Question("title1", "contents1").writeBy(user);
    }
}
