package qna.domain;

import qna.exception.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> answers;

    protected Answers() {
        this(new ArrayList<>());
    }

    public Answers(List<Answer> answers) {
        this.answers = new ArrayList<>(answers);
    }

    public static Answers empty() {
        return new Answers();
    }

    public void add(Answer answer) {
        if(this.answers.contains(answer)) {
            return;
        }
        this.answers.add(answer);
    }

    public boolean contains(Answer answer) {
        return this.answers.contains(answer);
    }

    public DeleteHistories deleteAll(User owner) throws CannotDeleteException {
        validateAnswerOwners(owner);

        List<DeleteHistory> deleteHistories = this.answers.stream()
                .map(Answer::delete)
                .collect(Collectors.toList());
        return new DeleteHistories(deleteHistories);
    }

    private void validateAnswerOwners(User owner) throws CannotDeleteException {
        if(!isOwnerAllMatch(owner)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    private boolean isOwnerAllMatch(User owner) {
        return this.answers.stream().allMatch(answer -> answer.isOwner(owner));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answers answers1 = (Answers) o;
        return Objects.equals(answers, answers1.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answers);
    }
}
