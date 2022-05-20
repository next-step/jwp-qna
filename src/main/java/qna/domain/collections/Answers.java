package qna.domain.collections;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import qna.domain.Answer;
import qna.domain.User;
import qna.exception.CannotDeleteException;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    public Answers() {
    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public Answers deleteAll(User questionWriter) throws CannotDeleteException {
        List<Answer> deleteAnswers = new ArrayList<>();
        for (Answer answer : answers) {
            addDeleteAnswers(deleteAnswers, answer);
        }
        for (Answer answer : deleteAnswers) {
            answer.delete(questionWriter);
        }
        return new Answers(deleteAnswers);
    }

    private void addDeleteAnswers(List<Answer> needDeleteAnswers, Answer answer) {
        if (!answer.isDeleted()) {
            needDeleteAnswers.add(answer);
        }
    }

    public List<Answer> getAnswers() {
        return answers;
    }
}
