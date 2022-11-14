package qna.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private final List<Answer> answers;

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public void addAnswer(Answer answer) {
        if (!answers.contains(answer)) {
            answers.add(answer);
        }
    }

    public boolean contains(Answer answer) {
        return this.answers.contains(answer);
    }

    public void removeAnswer(Answer answer) {
        answers.remove(answer);
    }

    public DeleteHistories delete(User loginUser) throws Exception {
        List<DeleteHistory> deleteHistoryList = new ArrayList<>();
        for (Answer answer : answers) {
            deleteHistoryList.add(answer.delete(loginUser));
        }
        return new DeleteHistories(deleteHistoryList);
    }
}
