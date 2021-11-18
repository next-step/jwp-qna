package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private final List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public Answers(List<Answer> answerGroup) {
        this.answers.addAll(answerGroup);
    }

    public static Answers of() {
        return new Answers();
    }

    public int size() {
        return answers.size();
    }

    private List<Answer> nonDeletedAnswers() {
        return answers.stream()
                .filter(item -> !item.isDeleted())
                .collect(Collectors.toList());
    }

    public void delete(User loginUser) throws CannotDeleteException {
        for (Answer answer : nonDeletedAnswers()) {
            answer.delete(loginUser);
        }
    }

    public List<Answer> getAnswerGroup() {
        return Collections.unmodifiableList(answers);
    }

    public boolean contains(Answer answer) {
        return this.answers.contains(answer);
    }

    public void add(Answer answer) {
        this.answers.add(answer);
    }
}
