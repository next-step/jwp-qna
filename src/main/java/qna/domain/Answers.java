package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private final List<Answer> answers;

    public Answers() {
        answers = new ArrayList<>();
    }

    public void add(Answer answer) {
        this.answers.add(answer);
    }

    public DeleteHistories delete(User loginUser) throws CannotDeleteException {
        DeleteHistories deleteHistories = new DeleteHistories();
        for (Answer answer : undeletedAnswers()) {
            deleteHistories.add(answer.delete(loginUser));
        }
        return deleteHistories;
    }

    public List<Answer> undeletedAnswers() {
        return answers.stream()
                .filter(answer -> !answer.isDeleted())
                .collect(Collectors.toList());
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(this.answers);
    }
}
