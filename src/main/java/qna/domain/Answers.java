package qna.domain;

import org.hibernate.annotations.Where;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    @Where(clause = "deleted = false")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public boolean allOwner(User owner) {
        return answers.stream()
            .allMatch(answer -> answer.isOwner(owner));
    }

    public List<DeleteHistory> delete() {
        return answers.stream()
            .map(Answer::delete)
            .collect(Collectors.toList());
    }
}
