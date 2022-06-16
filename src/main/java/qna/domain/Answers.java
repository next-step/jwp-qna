package qna.domain;

import java.util.List;
import qna.CannotDeleteException;

public class Answers {

    List<Answer> answers;

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public void deleteAll(User loginUser) throws CannotDeleteException {
        for (Answer answer : answers) {
            answer.delete(loginUser);
        }
    }
}
