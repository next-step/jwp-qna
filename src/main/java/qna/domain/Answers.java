package qna.domain;

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
