package qna.domain;

public class TestQuestionFactory {

    public static Question create(Long id, String title, String contents, User writer) {
        return new Question(id, title, contents, writer);
    }

    public static Question create(String title, String contents, User writer) {
        return new Question(title, contents, writer);
    }
}
