package qna.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answers;

    protected Answers() {
        this.answers = new ArrayList<>();
    }

    public DeleteHistories delete(User loginUser) {
        DeleteHistories deleteHistories = new DeleteHistories();
        for (Answer answer : answers) {
            final DeleteHistory deleteHistory = answer.delete(loginUser);
            deleteHistories.addHistory(deleteHistory);
        }
        return deleteHistories;
    }

    public void add(Answer answer) {
        this.answers.add(answer);
    }

    public List<Answer> getAnswers() {
        return new ArrayList<>(answers);
    }
}
