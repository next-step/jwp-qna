package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import qna.CannotDeleteException;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public Answers(final List<Answer> answers) {
        this.answers = new ArrayList<>(answers);
    }

    public void add(final Answer answer) {
        answers.add(answer);
    }

    public List<DeleteHistory> delete(final User loginUser) throws CannotDeleteException {
        final List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            deleteHistories.add(
                answer.delete(loginUser)
            );
        }
        return Collections.unmodifiableList(deleteHistories);
    }
}
