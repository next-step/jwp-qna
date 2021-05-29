package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class Answers {
    private List<Answer> answers;

    public Answers() {
        this.answers = new ArrayList<>();
    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<Answer> answers() {
        return this.answers;
    }

    public List<DeleteHistory> deleteAllAndHistory() {
        List<DeleteHistory> deleteHistories = new ArrayList<>();

        for (Answer answer : answers) {
            answer.setDeleted(true);
            deleteHistories.add(answer.deleteHistory());
        }

        return deleteHistories;
    }
}
