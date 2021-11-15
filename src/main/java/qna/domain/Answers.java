package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private final List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public Answers(List<Answer> answers) {
        this.answers.addAll(answers);
    }

    public void add(Answer answer) {
        if (!answers.contains(answer)) {
            answers.add(answer);
        }
    }

    public void remove(Answer answer) {
        answers.remove(answer);
    }

    public boolean contains(Answer answer) {
        return answers.contains(answer);
    }

    public int size() {
        return answers.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answers answers1 = (Answers) o;
        return Objects.equals(answers, answers1.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answers);
    }
}
