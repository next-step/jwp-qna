package qna.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private Set<Answer> answers = new HashSet<>();

    protected Answers() {
    }

    public void add(final Answer answer, final Question question) {
        answers.add(answer);
        if (answer.getQuestion() != question) {
            answer.toQuestion(question);
        }
    }

    public void remove(final Answer answer) {
        answers.remove(answer);
    }
}
