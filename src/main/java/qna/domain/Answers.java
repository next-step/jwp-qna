package qna.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import qna.CannotDeleteException;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<Answer> elements;

    public Answers() {
        this.elements = new ArrayList<>();
    }

    public void add(Answer answer) {
        elements.add(answer);
    }

    public DeleteHistories delete(User loginUser) throws CannotDeleteException {
        DeleteHistories deleteHistories = new DeleteHistories();
        for (Answer answer : elements) {
            deleteHistories.add(answer.delete(loginUser));
        }
        return deleteHistories;
    }

}
