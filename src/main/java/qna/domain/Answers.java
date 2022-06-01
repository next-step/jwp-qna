package qna.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private final List<Answer> answers;

    public Answers() {
        answers = new ArrayList<>();
    }

    public void add(Answer answer) {
        this.answers.add(answer);
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(this.answers);
    }
}
