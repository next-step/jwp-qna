package qna.domain;

public class TestQuestionFactory {

    public static Question create(Long id, String title, String contents) {
        return new Question(id, title, contents);
    }

    public static Question create(String title, String contents) {
        return new Question(title, contents);
    }
}
