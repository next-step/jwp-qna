package qna.domain;

import java.util.ArrayList;
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

    public List<DeleteHistory> deleteAll(User loginUser) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : values) {
            deleteHistories.add(answer.delete(loginUser));
        }

        return deleteHistories;
    }

    public List<Answer> getValues() {
        return values;
    }
}
