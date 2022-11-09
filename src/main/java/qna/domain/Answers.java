package qna.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private final List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public DeleteHistories delete(User loginUser) {
        List<DeleteHistory> list = new ArrayList<>();
        for (Answer answer : answers) {
            list.add(answer.delete(loginUser));
        }
        return new DeleteHistories(list);
    }

    public boolean notContains(Answer answer) {
        return !this.contains(answer);
    }

    public boolean contains(Answer answer) {
        return this.answers.contains(answer);
    }

    public void add(Answer answer) {
        this.answers.add(answer);
    }

    public void remove(Answer answer) {
        this.answers.remove(answer);
    }
}
