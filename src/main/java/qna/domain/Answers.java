package qna.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
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
}
