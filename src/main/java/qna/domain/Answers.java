package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public List<DeleteHistory> delete(User user) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        try {
            for (Answer answer : answers) {
                deleteHistories.add(answer.delete(user));
            }
        } catch (CannotDeleteException e) {
            resetDelete();
            throw new CannotDeleteException(e.getMessage());
        }
        return deleteHistories;
    }

    private void resetDelete() {
        for (Answer answer : answers) {
            answer.setDeleted(false);
        }
    }

    public boolean contains(Answer answer) {
        return this.answers.contains(answer);
    }

    public void remove(Answer answer) {
        this.answers.remove(answer);
    }

    public int size() {
        return this.answers.size();
    }

}
