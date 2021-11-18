package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {

    }

    public void add(Answer answer) {
        this.answers.add(answer);
    }

    public void delete(User loginUser) throws CannotDeleteException {
        for (Answer answer : this.answers) {
            answer.delete(loginUser);
        }
    }

    public List<Answer> getAnswers() {
        return answers;
    }
}
