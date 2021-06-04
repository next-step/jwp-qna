package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private final List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public Answers(final List<Answer> answerList) {
        answers.addAll(validAnswers(answerList));
    }

    private List<Answer> validAnswers(final List<Answer> answerList) {
        return Optional.ofNullable(answerList)
                .orElseThrow(IllegalArgumentException::new);
    }

    public void add(final Answer answer) {
        answers.add(answer);
    }

    public int size() {
        return answers.size();
    }

    public List<DeleteHistory> delete(final User user) throws CannotDeleteException {
        final List<DeleteHistory> deleteHistories = new ArrayList<>();

        for (final Answer answer : answers) {
            deleteHistories.add(answer.delete(user));
        }

        return deleteHistories;
    }
}
