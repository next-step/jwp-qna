package qna.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import qna.CannotDeleteException;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> answers;

    private Answers(List<Answer> answers) {
        this.answers = answers;
    }

    protected Answers() {

    }

    public static Answers createNewInstance() {
        return new Answers(new ArrayList<>());
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public List<DeleteHistory> deleteAll(User loginUser) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(loginUser));
        }
        return deleteHistories;
    }
}
