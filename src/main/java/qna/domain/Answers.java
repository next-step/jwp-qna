package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import qna.CannotDeleteException;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private final List<Answer> answers;

    protected Answers() {
        this.answers = new ArrayList<>();
    }

    public Answers(List<Answer> answerList) {
        this.answers = answerList;
    }

    public boolean contains(Answer answer) {
        return answers.contains(answer);
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for(Answer answer : answers){
            deleteHistories.add(answer.delete(loginUser));
        }
        return deleteHistories;
    }

    public boolean isAllDelete() {
        return this.answers.stream()
            .allMatch(Answer::isDeleted);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Answers answers1 = (Answers)o;
        return Objects.equals(answers, answers1.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answers);
    }
}
