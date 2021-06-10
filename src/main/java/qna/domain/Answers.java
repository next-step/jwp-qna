package qna.domain;

import qna.CannotDeleteException;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private final List<Answer> answers = new ArrayList<>();

    public Answers() {}

    public int size() {
        return answers.size();
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            addDeleteHistory(loginUser, deleteHistories, answer);
        }
        return deleteHistories;
    }

    private void addDeleteHistory(User loginUser, List<DeleteHistory> deleteHistories, Answer answer) throws CannotDeleteException {
        if (!answer.isDeleted()) {
            deleteHistories.add(answer.delete(loginUser));
        }
    }
}
