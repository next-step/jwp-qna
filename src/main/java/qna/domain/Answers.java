package qna.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers implements Serializable {

    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Answer> values;

    protected Answers() {
    }

    public Answers(List<Answer> answers) {
        this.values = answers;
    }

    public List<Answer> values() {
        return values;
    }

    public void add(Answer answer) {
        values.add(answer);
    }

    public List<DeleteHistory> delete() {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : values) {
            deleteHistories.add(answer.delete(answer.getWriter()));
        }

        return deleteHistories;
    }

    public void removeAnswer(Answer answer) {
        values = values.stream()
            .filter(answer1 -> !answer1.equals(answer))
            .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Answers answers = (Answers)o;
        return Objects.equals(values, answers.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }
}
