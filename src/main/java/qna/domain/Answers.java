package qna.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import qna.CannotDeleteException;

@Embeddable
public class Answers {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private final List<Answer> answers;

    protected Answers() {
        this(new ArrayList<>());
    }

    private Answers(final List<Answer> answers) {
        this.answers = answers;
    }

    public void add(final Answer answer) {
        this.answers.add(answer);
    }

    public DeleteHistories deleteAll(final User loginUser) throws CannotDeleteException {
        DeleteHistories deleteHistories = DeleteHistories.EMPTY;
        for (Answer answer : answers) {
            deleteHistories = deleteHistories.add(answer.delete(loginUser));
        }
        answers.clear();

        return deleteHistories;
    }

    public boolean contains(final Answer answer) {
        return answers.contains(answer);
    }
}
