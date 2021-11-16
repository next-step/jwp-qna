package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
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

    public List<DeleteHistory> deleteAll(User principal) throws CannotDeleteException {
        List<DeleteHistory> result = new ArrayList<>();
        // rollback 구현 없이 정합성 확인 후 작업을 하기 위해 두번 순회함
        for (Answer answer : answers) {
            throwExceptionWhenHasAnotherWriter(principal, answer);
        }
        for (Answer answer : answers) {
            result.add(answer.delete(principal));
        }
        return result;
    }

    private void throwExceptionWhenHasAnotherWriter(User principal, Answer answer) throws CannotDeleteException {
        if (!answer.isOwner(principal)) {
            throw new CannotDeleteException("작성자가 다른 답변이 포함되어 있습니다");
        }
    }

    public List<Answer> getAnswers() {
        return answers.stream()
                .filter(answer -> !answer.isDeleted())
                .collect(Collectors.toList());
    }

    public List<Answer> getDeletedAnswers() {
        return answers.stream()
                .filter(Answer::isDeleted)
                .collect(Collectors.toList());
    }
}
