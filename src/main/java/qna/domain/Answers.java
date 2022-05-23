package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private final List<Answer> answers;

    protected Answers() {
        this.answers = new ArrayList<>();
    }

    private Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public static Answers empty() {
        return new Answers();
    }

    public static Answers from(List<Answer> answers) {
        return new Answers(answers);
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public void deleteAll(User loginUser) throws CannotDeleteException {
        for (Answer answer : this.answers) {
            answer.delete(loginUser);
        }
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }
}
