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

    public void delete(User user) throws CannotDeleteException {
        for (Answer answer : answers) {
            answer.delete(user);
        }
    }

    public List<Answer> list(){
        return answers;
    }
}
