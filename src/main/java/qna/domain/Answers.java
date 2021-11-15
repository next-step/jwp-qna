package qna.domain;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import qna.CannotDeleteException;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> values;

    protected Answers() {
    }

    private Answers(List<Answer> values) {
        this.values = values;
    }

    public static Answers from(List<Answer> answers) {
        return new Answers(answers);
    }

    Answers notDeletedAnswers() {
        return from(values.stream()
            .filter(answer -> !answer.isDeleted())
            .collect(toList()));
    }

    public List<DeleteHistory> deleteAll(User loginUser) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : values) {
            deleteHistories.add(answer.delete(loginUser));
        }

        return deleteHistories;
    }

    public static Answers empty() {
        return Answers.from(new ArrayList<>());
    }

    public void add(Answer answer) {
        this.values.add(answer);
    }

    public void remove(Answer answer) {
        this.values.remove(answer);
    }

    public List<Answer> getValues() {
        return this.values;
    }
}
