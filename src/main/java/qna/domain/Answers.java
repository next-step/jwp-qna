package qna.domain;

import qna.CannotDeleteException;
import qna.ForbiddenException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
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

    public List<DeleteHistory> delete(User user) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            final DeleteHistory deleteHistory = answer.delete(user);
            deleteHistories.add(deleteHistory);
        }
        return deleteHistories;
    }
}
