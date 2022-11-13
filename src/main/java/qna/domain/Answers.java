package qna.domain;

import org.hibernate.annotations.Where;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Embeddable
public class Answers implements Iterable<Answer> {

    @OneToMany(mappedBy = "question")
    @Where(clause = "deleted = false")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public void deleteAnswer(Answer deletedAnswer) {
        this.answers
            .stream()
            .filter(deletedAnswer::equals)
            .forEach(answer -> answer.setDeleted(true));
    }

    public void refreshAnswerWithoutDelete() {
        this.answers = this.answers
            .stream()
            .filter(answer -> !answer.isDeleted())
            .collect(Collectors.toList());
    }

    @Override
    public Iterator<Answer> iterator() {
        return this.answers.iterator();
    }

    @Override
    public void forEach(Consumer<? super Answer> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<Answer> spliterator() {
        return Iterable.super.spliterator();
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }
}
