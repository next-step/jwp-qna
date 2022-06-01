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
    private final List<Answer> answers;

    public Answers() {
        this.answers = new ArrayList<>();
    }

    public void add(final Answer answer) {
        answers.add(answer);
    }

    public List<DeleteHistory> deleteAll(final User deleteBy) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            deleteHistories.add(answer.deleteAnswer(deleteBy));
        }
        return deleteHistories;
    }

    @Override
    public String toString() {
        return "Answers{" +
                "answers=" + answers +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Answers answers1 = (Answers) o;
        return Objects.equals(answers, answers1.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answers);
    }
}
