package qna.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "question",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public void addAnswer(Answer answer) {
        if (!answers.contains(answer)) {
            answers.add(answer);
        }
    }

    public void deleteAll(User loginUser) {
        answers.forEach(answer -> answer.delete(loginUser));
    }

}
