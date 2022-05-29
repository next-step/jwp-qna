package qna.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import qna.CannotDeleteException;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private Set<Answer> answers = new HashSet<>();

    protected Answers() {
    }

    public void add(final Answer answer) {
        answers.add(answer);
    }

    public void remove(final Answer answer) {
        answers.remove(answer);
    }

    public boolean allWrittenBy(final User writer) {
        return answers.stream()
                .allMatch(answer -> answer.isOwner(writer));
    }

    public List<DeleteHistory> delete(final User writer) throws CannotDeleteException {
        final List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (final Answer answer : answers) {
            deleteHistories.add(answer.delete(writer));
        }
        return deleteHistories;
    }
}
