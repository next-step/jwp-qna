package qna.domain.wrapper;

import qna.domain.Answer;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    public void add(Answer answer) {
        if (!answers.contains(answer)) {
            this.answers.add(answer);
        }
    }
}
