package qna.domain;

import java.util.List;
import qna.CannotDeleteException;

public class Answers {

    private List<Answer> values;

    private Answers(List<Answer> values) {
        this.values = values;
    }

    public static Answers from(List<Answer> answers) {
        return new Answers(answers);
    }

    public Answers deleteAll(User loginUser) throws CannotDeleteException {
        for (Answer answer : values) {
            answer.delete(loginUser);
        }

        return this;
    }

    public List<Answer> getValues() {
        return values;
    }
}
