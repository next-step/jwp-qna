package qna.domain.answer.answers;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import qna.CannotDeleteException;
import qna.domain.DeleteHistory;
import qna.domain.answer.Answer;
import qna.domain.user.User;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    public void add(Answer answer) {
        answers.add(answer);
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(loginUser));
        }
        return deleteHistories;
    }
}
