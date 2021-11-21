package qna.domain;

import org.hibernate.annotations.Where;
import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Answers {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    @Where(clause = "deleted = false")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public static Object of(Answer... answers) {
        return new Answers(Arrays.asList(answers));
    }

    public DeleteHistories delete(User user) throws CannotDeleteException {

        DeleteHistories deleteHistories = new DeleteHistories();
        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(user));
        }

        return deleteHistories;
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Answers answers1 = (Answers) o;
        return Objects.equals(answers, answers1.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answers);
    }

    public boolean isContain(Answer answer) {
        return answers.contains(answer);
    }

    public void remove(Answer answer) {
        answers.remove(answer);
    }
}

