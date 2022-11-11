package qna.domain;

public class TestQuestionFactory {
    public static Question create(User writer) {
        return new Question("title", "contents", writer);
    }
}
