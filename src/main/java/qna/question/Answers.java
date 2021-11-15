package qna.question;

import qna.exception.CannotDeleteException;
import qna.answer.Answer;
import qna.deletehistory.DeleteHistory;
import qna.user.User;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public Answers(List<Answer> answers){
        this.answers = answers;
    }

    public void add(final Answer answer) {
        answers.add(answer);
    }

    public void delete() {
        for (Answer answer : answers) {
            answer.delete();
        }
    }

    public void throwExceptionNotDeletableAnswers(final User loginUser) throws CannotDeleteException {
        for (Answer answer : answers) {
            answer.throwExceptionNotDeletableUser(loginUser);
        }
    }

    public List<DeleteHistory> createDeleteHistories(){
        return answers.stream()
                .map(DeleteHistory::fromAnswer)
                .collect(Collectors.toList());
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
