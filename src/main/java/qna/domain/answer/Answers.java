package qna.domain.answer;

import qna.CannotDeleteException;
import qna.domain.deletehistory.DeleteHistory;
import qna.domain.user.User;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "question")
    private final List<Answer> answers = new ArrayList<>();

    public List<DeleteHistory> deleteAnswer(User loginUser) throws CannotDeleteException {
        final List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            deleteHistories.add(answer.deleteByUser(loginUser));
        }
        return deleteHistories;
    }

    public void remove(Answer answer) {
        this.answers.remove(answer);
    }

    public void add(Answer answer) {
        this.answers.add(answer);
    }
}
