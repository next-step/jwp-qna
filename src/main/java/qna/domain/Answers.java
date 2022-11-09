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

    public void delete(User user) throws CannotDeleteException {
        try {
            for (Answer answer : answers) {
                answer.delete(user);
            }
        } catch (CannotDeleteException e) {
            restoreDelete();
            throw new CannotDeleteException(e.getMessage());
        }
    }

    private void restoreDelete() {
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
