package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    private List<Answer> content;

    protected Answers() {
    }

    public Answers(List<Answer> content) {
        this.content = content;
    }

    public static Answers empty() {
        return new Answers();
    }

    public void add(Answer answer) {
        content.add(answer);
    }

    public List<DeleteHistory> delete(User user) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : content) {
            deleteHistories.add(answer.delete(user));
        }
        return deleteHistories;
    }
}
