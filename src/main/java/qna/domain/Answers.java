package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public void add(Answer answer) {
        this.answers.add(answer);
    }

    public List<DeleteHistory> delete(User loginUser, LocalDateTime deletedAt) throws CannotDeleteException {
        List<Answer> notDeletedAnswers = this.answers.stream().filter(answer -> !answer.isDeleted()).collect(Collectors.toList());

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : notDeletedAnswers) {
            deleteHistories.add(answer.delete(loginUser, deletedAt));
        }

        return deleteHistories;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Answers)) return false;
        Answers answers1 = (Answers) o;
        return Objects.equals(getAnswers(), answers1.getAnswers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAnswers());
    }

    @Override
    public String toString() {
        return "Answers{" +
                "answers=" + answers +
                '}';
    }
}
