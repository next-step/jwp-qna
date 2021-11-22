package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public Answers(final List<Answer> answers) {
        // this.answers = new ArrayList<>(answers);
        answers.forEach(this::add);
    }

    public void add(final Answer answer) {
        if (answers.contains(answer)) {
            throw new IllegalArgumentException("이미 등록된 답변입니다.");
        }
        if (!answers.isEmpty()) {
            validateQuestion(answer);
        }
        answers.add(answer);
    }

    private void validateQuestion(final Answer answer) {
        if (!answers.get(0).getQuestion().equals(answer.getQuestion())) {
            throw new IllegalArgumentException("이미 등록된 답변입니다.");
        }
    }

    public DeleteHistories delete(final User loginUser) {
        final DeleteHistories deleteHistories = new DeleteHistories();
        for (Answer answer : answers) {
            deleteHistories.add(
                answer.delete(loginUser)
            );
        }
        return deleteHistories;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Answers answers1 = (Answers) o;
        return Objects.equals(answers, answers1.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answers);
    }
}
