package qna.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private final List<Answer> answers = new ArrayList<>();

    public Answers() {}

    public int size() {
        return answers.size();
    }

    public void add(Answer answer) {
        answers.add(answer);
    }
}
