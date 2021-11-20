package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    protected Answers() {
    }

    public List<DeleteHistory> delete(User loginUser) {
        validateOwner(loginUser);
        return answers.stream()
            .map(answer -> answer.delete(loginUser))
            .collect(Collectors.toList());
    }

    protected void validateOwner(User loginUser) {
        for (Answer answer : answers) {
            answer.validateOwner(loginUser);
        }
    }

    public boolean add(Answer answer) {
        if (answers.contains(answer)) {
            return false;
        }
        return answers.add(answer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Answers)) {
            return false;
        }
        Answers answers1 = (Answers)o;
        return Objects.equals(answers, answers1.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answers);
    }
}
