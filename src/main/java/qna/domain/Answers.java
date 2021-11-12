package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    protected Answers() {

    }

    public void checkIsOwner(User writer) {
        for (Answer answer : answers) {
            checkIsOwner(writer, answer);
        }
    }

    private void checkIsOwner(User writer, Answer answer) {
        answer.checkIsOwner(writer);
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public void delete() {
        for (Answer answer : answers) {
            answer.delete();
        }
    }

    public List<DeleteHistory> delete(User writer) {
        checkIsOwner(writer);
        delete();
        return getDeleteHistories();
    }

    public List<DeleteHistory> getDeleteHistories() {
        return answers.stream()
                .map(Answer::getDeleteHistory)
                .collect(Collectors.toList());
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(answers);
    }
}
