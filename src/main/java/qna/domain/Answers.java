package qna.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.requireNonNull;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
    private  List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public Answers(List<Answer> answers) {
        this.answers = requireNonNull(answers, "answers");
    }

    public void remove(Answer answer) {
        answers.remove(answer);
    }

    public void add(Answer answer) {
        if (!answers.contains(answer)) {
            answers.add(answer);
        }
    }

    public List<DeleteHistory> removeAll(User loginUser) {
        final List<DeleteHistory> deleteHistories = new ArrayList<>(answers.size());
        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(loginUser));
        }
        return deleteHistories;
    }

    public List<Answer> get() {
        return answers;
    }
}
