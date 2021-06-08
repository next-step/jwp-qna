package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Answer> answers;

    public Answers() {
        this.answers = new ArrayList<>();
    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public void add(Answer answer) {
        this.answers.add(answer);
    }

    public void add(Answers answers) {
        this.answers.addAll(answers.asList());
    }

    public void toQuestion(Question question) {
        for (Answer answer : this.answers) {
            answer.toQuestion(question);
        }
    }

    public void delete(User loginUser) throws CannotDeleteException {
        for (Answer answer : this.answers) {
            answer.delete(loginUser);
        }
    }

    public List<Answer> asList() {
        return Collections.unmodifiableList(this.answers);
    }
}
