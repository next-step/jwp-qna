package qna.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public DeleteHistories delete(User user) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for(Answer answer: this.answers) {
            deleteHistories.add(answer.delete(user));
        }
        return new DeleteHistories(deleteHistories);
    }

    public void removeAnswer(Answer answer) {
        answers.remove(answer);
    }

    public void addAnswer(Answer answer) {
        if(!answers.contains(answer)) {
            answers.add(answer);
        }
    }

    public int answersCount() {
        return answers.size();
    }

    public boolean contains(Answer answer) {
        return this.answers.contains(answer);
    }
}
