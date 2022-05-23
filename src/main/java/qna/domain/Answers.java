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

    public DeleteHistories deleteAll(User loginUser) throws CannotDeleteException {
        DeleteHistories deleteHistories = DeleteHistories.empty();
        for (Answer answer : this.answers) {
            deleteHistories.addDeleteHistory(answer.delete(loginUser));
        }
        return deleteHistories;
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }
}
