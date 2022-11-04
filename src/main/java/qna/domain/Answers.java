package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class Answers {

    private final List<Answer> answers;

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public List<DeleteHistory> delete(User user) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        validateSameUser(user);
        for(Answer answer: this.answers) {
            deleteHistories.add(answer.delete(user));
        }
        return deleteHistories;
    }

    private void validateSameUser(User user) {
        for(Answer answer: this.answers) {
            answer.validateSameUser(user);
        }
    }
}
