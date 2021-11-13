package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    public void add(Answer answer) {
        if (!answers.contains(answer)) {
            answers.add(answer);
        }
    }

    public boolean contains(Answer answer) {
        return answers.contains(answer);
    }

    public int size() {
        return answers.size();
    }
}
