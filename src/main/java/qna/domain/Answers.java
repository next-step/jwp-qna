package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class Answers {

    private final List<Answer> answers;

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public void deleteAnswers(User loginUser) {
        for (Answer answer : answers) {
            answer.deletedByUser(loginUser);
        }
    }

    public List<Answer> getAnswers() {
        return new ArrayList<>(this.answers);
    }
}
