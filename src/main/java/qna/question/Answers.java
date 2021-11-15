package qna.question;

import qna.CannotDeleteException;
import qna.answer.Answer;
import qna.user.User;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public Answers(List<Answer> answers){
        this.answers = answers;
    }

    public void addAnswer(final Answer answer) {
        answers.add(answer);
    }

    public void changeDeletedAnswer() {
        for (Answer answer : answers) {
            answer.delete();
        }
    }

    public void throwExceptionNotDeletableAnswers(final User loginUser) throws CannotDeleteException {
        for (Answer answer : answers) {
            answer.throwExceptionNotDeletableUser(loginUser);
        }
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
