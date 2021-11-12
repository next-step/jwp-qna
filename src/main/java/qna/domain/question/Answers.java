package qna.domain.question;

import qna.domain.answer.Answer;
import qna.domain.user.User;
import qna.exception.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> answers;

    protected Answers() {
        this.answers = new ArrayList<>();
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public void changeAnswerState(User loginUser) throws CannotDeleteException {
        for (Answer answer : answers) {
            answer.changeDeleteState(loginUser);
        }
    }

    public List<Answer> getAnswers() {
        return answers;
    }
}
