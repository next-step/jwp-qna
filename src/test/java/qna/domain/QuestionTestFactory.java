package qna.domain;

import qna.domain.question.Question;
import qna.domain.user.User;

public class QuestionTestFactory {

    private static final String DUMMY_CONTENT = "DUMMY_CONTENT";
    private static final String DUMMY_TITLE = "DUMMY_TITLE";

    public static Question create(User writer) {
        return create(DUMMY_TITLE, DUMMY_CONTENT, writer, false);
    }

    public static Question create(String title, String contents, User writer) {
        return create(title, contents, writer, false);
    }

    public static Question create(String title, String contents, User writer, boolean deleted) {
        return new Question(title, contents, deleted).writeBy(writer);
    }
}
