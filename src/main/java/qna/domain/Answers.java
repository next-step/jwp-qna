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

    public void deleteAnswer(Answer deletedAnswer) {
        this.answers
            .stream()
            .filter(deletedAnswer::equals)
            .forEach(answer -> answer.setDeleted(true));
    }

    public void refreshAnswerWithoutDelete() {
        this.answers = this.answers
            .stream()
            .filter(answer -> !answer.isDeleted())
            .collect(Collectors.toList());
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public boolean allOwner(User owner) {
        return answers.stream()
            .allMatch(answer -> answer.isOwner(owner));
    }

    public List<DeleteHistory> allDeleteAndGetHistory() {
        return answers.stream()
            .map(Answer::deleteAndGetHistory)
            .collect(Collectors.toList());
    }

    public int size() {
        return this.answers.size();
    }
}
