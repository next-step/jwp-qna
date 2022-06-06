package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private final List<Answer> answers = new ArrayList<>();

    public void add(Answer answer) {
        this.answers.add(answer);
    }

    public DeleteHistories delete(User loginUser) throws CannotDeleteException {
        DeleteHistories deleteHistories = new DeleteHistories();
        for (Answer answer : answers) {
            DeleteHistory delete = answer.delete(loginUser);
            deleteHistories.add(delete);
        }
        return deleteHistories;
    }

    public boolean contains(Answer answer) {
        return this.answers.contains(answer);
    }

    public Answer get(int index) {
        return this.answers.get(index);
    }
}
