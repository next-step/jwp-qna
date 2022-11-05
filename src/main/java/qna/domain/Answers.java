package qna.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import qna.CannotDeleteException;
import qna.domain.message.ExceptionMessage;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private Set<Answer> values;

    protected Answers() {
        this.values = new HashSet<>();
    }

    private Answers(Set<Answer> values) {
        this.values = values;
    }

    public static Answers init() {
        return new Answers(new HashSet<>());
    }

    public static Answers from(List<Answer> answers) {
        return new Answers(new HashSet<>(answers));
    }

    public void add(Answer answer) {
        this.values.add(answer);
    }

    public void remove(Answer answer) {
        this.values.remove(answer);
    }

    public boolean contains(Answer answer) {
        return this.values.contains(answer);
    }

    public void deleted(User writer) throws CannotDeleteException {
        boolean isNotOwner = values.stream().anyMatch(answer -> !answer.isOwner(writer));
        if (isNotOwner) {
            throw new CannotDeleteException(ExceptionMessage.NO_PERMISSION_DELETE_ANSWER);
        }

        values.forEach(Answer::deleted);
    }

    public boolean isAllDeleted() {
        return values.stream().allMatch(Answer::isDeleted);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Answers answers = (Answers) o;
        return values.equals(answers.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }
}
