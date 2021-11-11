package qna.domain;

public class QuestionTestFactory {
    private QuestionTestFactory() {
        throw new UnsupportedOperationException();
    }

    public static Question create(String title, String contents, User user) {
        return new Question(title, contents).writeBy(user);
    }

    public static Question create(Long id, String title, String contents, User user) {
        return new Question(id, title, contents).writeBy(user);
    }
}
