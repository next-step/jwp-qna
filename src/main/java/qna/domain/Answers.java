package qna.domain;

import qna.CannotDeleteException;
import qna.ForbiddenException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private List<Answer> answers;

    protected Answers() {
        this.answers = new ArrayList<>();
    }

    public void add(Answer answer) {
        if (answers.contains(answer)) {
            throw new ForbiddenException("중복된 answer 값");
        }
        this.answers.add(answer);
    }

    public DeleteHistories delete(User user) throws CannotDeleteException {
        DeleteHistories deleteHistories = new DeleteHistories();
        for (Answer answer : answers) {
            DeleteHistory deleteHistory = answer.delete(user);
            deleteHistories.addHistory(deleteHistory);
        }
        return deleteHistories;
    }
}
