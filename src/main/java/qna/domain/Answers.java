package qna.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.Assert;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    private Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public static Answers from(List<Answer> answers) {
        return new Answers(answers);
    }

    public static Answers create() {
        return new Answers();
    }

    public void add(Answer answer) {
        Assert.notNull(answer, "'answer' must not be null");
        this.answers.add(answer);
    }

    public List<DeleteHistory> delete(User user) {
        return null;
    }
}
