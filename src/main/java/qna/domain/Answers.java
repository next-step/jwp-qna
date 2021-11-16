package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private final List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public Answers(List<Answer> answers) {
        this.answers.addAll(answers);
    }

    public void add(Answer answer) {
        if (!answers.contains(answer)) {
            answers.add(answer);
        }
    }

    public void remove(Answer answer) {
        answers.remove(answer);
    }

    public boolean contains(Answer answer) {
        return answers.contains(answer);
    }

    public int size() {
        return answers.size();
    }

    public void deleteAll(User principal) throws CannotDeleteException {
        for (Answer answer : answers) {
            throwExceptionWhenHasAnotherWriter(principal, answer);
        }
        for (Answer answer : answers) {
            answer.delete(principal);
        }
    }

    private void throwExceptionWhenHasAnotherWriter(User principal, Answer answer) throws CannotDeleteException {
        if (!answer.isOwner(principal)) {
            throw new CannotDeleteException("작성자가 다른 답변이 포함되어 있습니다");
        }
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public List<Answer> getDeletedAnswers() {
        return answers.stream()
                .filter(Answer::isDeleted)
                .collect(Collectors.toList());
    }
}
