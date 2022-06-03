package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private final List<Answer> values = new ArrayList<>();

    public void add(Answer answer) {
        this.values.add(answer);
    }

    public boolean contains(Answer answer) {
        return this.values.contains(answer);
    }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : values) {
            deleteHistories.add(answer.delete(loginUser));
        }
        return deleteHistories;
    }

    public int size() {
        return values.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answers answers = (Answers) o;
        return Objects.equals(values, answers.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }
}
