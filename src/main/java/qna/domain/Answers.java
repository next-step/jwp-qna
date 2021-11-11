package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private Set<Answer> answers = new HashSet<>();

    public void add(Answer answer) {
        answers.add(answer);
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void validateAnswers(User writer) {
        for (Answer answer : answers) {
            validateOwner(writer, answer);
        }
    }

    public void deleteAnswers() {
        for (Answer answer : getAnswers()) {
            answer.delete();
        }
    }

    public List<DeleteHistory> getDeleteHistories() {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : getAnswers()) {
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter()));
        }
        return deleteHistories;
    }

    private void validateOwner(User writer, Answer answer) {
        if (!answer.isOwner(writer)) {
            throw new CannotDeleteException(ErrorMessage.EXISTS_ANSWER_OF_OTHER);
        }
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
