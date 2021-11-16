package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public void removeAnswer(Answer answer) {
        answers.remove(answer);
    }
}
