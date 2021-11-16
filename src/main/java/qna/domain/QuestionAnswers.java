package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Where;

@Embeddable
public class QuestionAnswers {
    @Where(clause = "deleted = false")
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Answer> answers = new ArrayList<>();

    protected QuestionAnswers() {
    }

    List<DeleteHistory> deleteBy(final User loginUser) {
        checkAuthorities(loginUser);
        return deleteAnswersBy(loginUser);
    }

    private void checkAuthorities(final User loginUser) {
        for (Answer answer : answers) {
            answer.checkAuthority(loginUser);
        }
    }

    private List<DeleteHistory> deleteAnswersBy(final User loginUser) {
        final List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            DeleteHistory history = answer.deleteBy(loginUser);
            deleteHistories.add(history);
        }
        return deleteHistories;
    }

    void remove(final Answer answer) {
        answers.remove(answer);
    }

    void add(final Answer answer) {
        answers.add(answer);
        validate();
    }

    private void validate() {
        if (!answers.isEmpty()) {
            final Question question = answers.get(0).getQuestion();
            validateSameQuestion(question);
        }
    }

    private void validateSameQuestion(final Question question) {
        final boolean isAllSameQuestion = answers.stream()
            .allMatch(answer -> answer.getQuestion().equals(question));
        if (!isAllSameQuestion) {
            throw new IllegalArgumentException("QuestionAnswers객체는 하나의 Question객체에 종속된 객체입니다.");
        }
    }

    boolean contains(final Answer answer) {
        return answers.contains(answer);
    }

    boolean allMatch(final Predicate<Answer> predicate) {
        return answers.stream()
            .allMatch(predicate);
    }
}
