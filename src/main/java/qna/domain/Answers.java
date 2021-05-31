package qna.domain;

import qna.exception.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question", orphanRemoval = true)
    private Set<Answer> answers = new HashSet<>();

    public boolean contains(Answer answer) {
        return answers.contains(answer);
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public int size() {
        return answers.size();
    }

    public void delete(User deleter) throws CannotDeleteException {
        answers.forEach(answer -> answer.delete(deleter));
    }

    public void forEach(Consumer<? super Answer> action) {
        Objects.requireNonNull(action);
        for (Answer answer : answers) {
            action.accept(answer);
        }
    }
}
