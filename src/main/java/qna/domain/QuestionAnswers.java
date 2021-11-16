package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Where;

@Embeddable
public class QuestionAnswers {
    @Where(clause = "deleted = false")
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private List<Answer> answers = new ArrayList<>();

    protected QuestionAnswers() {
    }

    List<DeleteHistory> deleteBy(final User loginUser) {
        final List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (final Answer answer : answers) {
            DeleteHistory history = answer.deleteBy(loginUser);
            deleteHistories.add(history);
        }
        return deleteHistories;
    }

    void add(final Answer answer) {
        answers.add(answer);
    }

    boolean contains(final Answer answer) {
        return answers.contains(answer);
    }

    boolean allMatch(final Predicate<Answer> predicate) {
        return answers.stream()
            .allMatch(predicate);
    }
}
