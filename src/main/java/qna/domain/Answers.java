package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import qna.CannotDeleteException;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private final List<Answer> elements;

    protected Answers() {
        this.elements = new ArrayList<>();
    }

    private Answers(List<Answer> elements) {
        this.elements = elements;
    }

    public static Answers createEmpty() {
        return new Answers(new ArrayList<>());
    }

    public DeleteHistories deleteAll(User owner) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : this.elements) {
            deleteHistories.add(answer.toDeleted(owner));
        }
        return DeleteHistories.of(deleteHistories);
    }

    public void add(Answer elements) {
        this.elements.add(elements);
    }

    public List<Answer> findReadOnlyElements() {
        return Collections.unmodifiableList(this.elements);
    }
}
