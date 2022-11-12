package qna.domain.entity;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    public void delete(User loginUser) throws CannotDeleteException {
        for (Answer answer : answers) {
            answer.delete(loginUser);
        }
    }

    public void add(Answer answer) {
        this.answers.add(answer);
    }

    public int size() {
        return this.answers.size();
    }

    public void clear() {
        this.answers.clear();
    }
}
