package qna.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Where;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    @Where(clause = "deleted = false")
    private final List<Answer> answers = new ArrayList<>();

    public void add(final Answer answer) {
        answers.add(answer);
    }

    public List<Answer> getAnswers() {
        return answers;
    }
}
