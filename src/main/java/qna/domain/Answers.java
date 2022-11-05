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

    public List<Answer> getAnswers() {
        return answers;
    }

    public List<DeleteHistory> delete(User user) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        validateSameUser(user);
        for(Answer answer: this.answers) {
            deleteHistories.add(answer.delete(user));
        }
        return deleteHistories;
    }

    private void validateSameUser(User user) {
        for(Answer answer: this.answers) {
            answer.validateSameUser(user);
        }
    }

    public void removeAnswer(Answer answer) {
        answers.remove(answer);
    }

    public void addAnswer(Answer answer) {
        if(!answers.contains(answer)) {
            answers.add(answer);
        }
    }
}
