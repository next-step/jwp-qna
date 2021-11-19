package qna.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public List<Answer> getValue() {
        return answers;
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public DeleteHistories delete(User loginUser, LocalDateTime deletedTime) {
        DeleteHistories deleteHistories = new DeleteHistories();

        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(loginUser, deletedTime));
        }
        return deleteHistories;
    }
}
