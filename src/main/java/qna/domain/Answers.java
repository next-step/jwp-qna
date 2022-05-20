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

    public void removeAll(User loginUser) {
        for (Answer answer : answers) {
            answer.delete(loginUser);
        }
    }

    public List<Answer> get() {
        return answers;
    }
}
