package qna.domain;

import java.util.ArrayList;
import java.util.List;

import qna.CannotDeleteException;

public class Answers {
    private final List<Answer> answers;

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public int size() {
        return this.answers.size();
    }

    public List<DeleteHistory> deleteAllByWriter(User loginUser) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : this.answers) {
            deleteHistories.add(answer.deleteByWriter(loginUser));
        }
        return deleteHistories;
    }

    public List<Answer> getAnswers() {
        return new ArrayList<>(this.answers);
    }
}
