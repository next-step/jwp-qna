package qna.fixtures;

import qna.domain.Question;
import qna.domain.User;

public class QuestionTestFixture {

    public static Question createQuestionWithWriter(User writer) {
        return new Question(writer, "title", "contents");
    }

    public static Question createQuestion() {
        return createQuestionWithId(null);
    }

    public static Question createQuestionWithId(Long id) {
        User writer = UserTestFixture.createUser();
        return new Question(id, writer, "title", "contents");
    }

    public static Question createQuestionWithIdAndWriter(Long id, User writer) {
        return new Question(id, writer, "title", "contents");
    }
}
