package qna.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import qna.exception.CannotDeleteException;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public void add(Answer answer) {
        if (!this.answers.contains(answer)) {
            this.answers.add(answer);
        }
    }

    public List<Answer> list() {
        return answers;
    }

    public boolean contains(Answer answer) {
        return this.answers.contains(answer);
    }

    public void validateExistOtherAnswer(User loginUser) throws CannotDeleteException {
        for (Answer answer : this.answers) {
            answer.validateRemovable(loginUser);
        }
    }
}
