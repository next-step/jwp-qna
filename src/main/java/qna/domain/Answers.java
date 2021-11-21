package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import qna.CannotDeleteException;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    protected Answers() {
    }

    public DeleteHistories delete(User loginUser) {
        validateOwner(loginUser);

        return new DeleteHistories(answers.stream()
            .map(Answer::delete)
            .collect(Collectors.toList()));
    }

    protected void validateOwner(User loginUser) {
        for (Answer answer : answers) {
            validateOwner(answer, loginUser);
        }
    }

    private void validateOwner(Answer answer, User loginUser) {
        if (!answer.isOwner(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    public boolean add(Answer answer) {
        return answers.add(answer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Answers)) {
            return false;
        }
        Answers answers1 = (Answers)o;
        return Objects.equals(answers, answers1.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answers);
    }
}
