package qna.domain;

import qna.CannotDeleteException;

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

    public Answers(List<Answer> answers) {
        this.answers = new ArrayList<>(answers);
    }

    public DeleteHistories deleteAll(User questionWriter) throws CannotDeleteException {
        validateWriter(questionWriter);

        DeleteHistories deleteHistories = new DeleteHistories(new ArrayList<>());
        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(questionWriter));
        }

        return deleteHistories;
    }

    private void validateWriter(User user) throws CannotDeleteException {
        for(Answer answer : answers) {
            answer.validateWriter(user);
        }
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public void removeAnswer(Answer answer) {
        answers.remove(answer);
    }

    public boolean contains(Answer answer) {
        return answers.contains(answer);
    }
}
