package qna.domain;

import qna.CannotDeleteException;
import java.util.List;

public class Answers {
    private static List<Answer> answers;

    private Answers(List<Answer> answers, User loginUser) throws CannotDeleteException {
        validateAnswerWriterSameAsLoginUser(answers, loginUser);
        Answers.answers = answers;
    }

    public static Answers createAnswers(List<Answer> answers, User loginUser) throws CannotDeleteException {
        return new Answers(answers, loginUser);
    }

    private void validateAnswerWriterSameAsLoginUser(List<Answer> answers, User loginUser) throws CannotDeleteException {
        for (Answer answer : answers) {
            answer.sameAsUser(loginUser);
        }
    }

    public List<Answer> getAnswers() {
        return answers;
    }
}
