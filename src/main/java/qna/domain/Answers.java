package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import qna.exception.CannotDeleteException;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public void add(Answer answer) {
        if (!this.answers.contains(answer)) {
            this.answers.add(answer);
        }
    }

    public List<Answer> list() {
        return answers;
    }

    public boolean contains(Answer answer) {
        return this.answers.contains(answer);
    }

    public void validateExistAnswerByOtherUser(User loginUser) throws CannotDeleteException {
        for (Answer answer : this.answers) {
            answer.validateRemovable(loginUser);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Answers answers1 = (Answers) o;
        return Objects.equals(answers, answers1.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answers);
    }
}
