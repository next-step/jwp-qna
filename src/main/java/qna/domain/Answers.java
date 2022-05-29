package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private final Set<Answer> answers = new HashSet<>();

    public Answers() {

    }

    public Answers(final Answer answer) {
        answers.add(answer);
    }

    public Answers(final List<Answer> answers) {
        if (Objects.isNull(answers)) {
            throw new IllegalArgumentException("invalid parameter");
        }
        this.answers.addAll(answers);
    }

    public int size() {
        return answers.size();
    }

    public void addAnswer(final Answer answer) {
        answers.add(answer);
    }

    public Answers findAnswerBy(final DeletedType state) {
        return new Answers(answers.stream()
                .filter(answer -> !answer.isDeleted())
                .collect(Collectors.toList()));
    }

    public Answers findAnswerBy(final User writer) {
        return new Answers(answers.stream()
                .filter(answer -> answer.isOwner(writer))
                .collect(Collectors.toList()));
    }

    public boolean isDifferenceAnswerBy(final User writer) {
        return answers.stream().anyMatch(answer -> !answer.isOwner(writer));
    }

    public DeleteHistories remove(final User writer) throws CannotDeleteException {
        if (isDifferenceAnswerBy(writer)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
        return new DeleteHistories(this.answers.stream().map(Answer::remove).collect(Collectors.toList()));
    }

    public boolean isContains(final Answer answer) {
        return this.answers.contains(answer);
    }

    public List<Answer> getAnswers() {
        return new ArrayList<>(answers);
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
