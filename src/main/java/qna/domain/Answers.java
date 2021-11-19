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

    public void addAnswer(final Answer answer) {
        answers.add(answer);
    }

    public DeleteHistories delete(User loginUser) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();

        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(loginUser));
        }

        return DeleteHistories.from(deleteHistories);
    }

    public List<Answer> answers() {
        return Collections.unmodifiableList(answers);
    }

    public int size() {
        return answers.size();
    }
}
