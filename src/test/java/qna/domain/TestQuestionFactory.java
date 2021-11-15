package qna.domain;

public class TestQuestionFactory {

    private TestQuestionFactory() {

    }

    public static Question create(String title, String contents, User write) {
        return new Question(title, contents).writeBy(write);
    }

}
