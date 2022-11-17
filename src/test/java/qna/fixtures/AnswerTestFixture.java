package qna.fixtures;

import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

public class AnswerTestFixture {

    public static Answer createAnswer() {
        return createAnswerWithId(null);
    }

    public static Answer createAnswerWithId(Long id) {
        User writer = UserTestFixture.createUser();
        Question question = QuestionTestFixture.createQuestionWithWriter(writer);
        return new Answer(id, writer, question, "sample");
    }

    public static Answer createAnswerWithWriter(User writer) {
        Question question = QuestionTestFixture.createQuestionWithWriter(writer);
        return new Answer(writer, question, "sample");
    }

    public static Answer createAnswerWithWriterAndQuestion(User writer, Question question) {
        return new Answer(writer, question, "sample");
    }

    public static Answer createAnswerWithIdAndWriterAndQuestion(Long id, User writer, Question question) {
        return new Answer(id, writer, question, "sample");
    }
}
