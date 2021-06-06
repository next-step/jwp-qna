package qna.domain;

import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    @ReadOnlyProperty
    private final List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public DeleteHistories deleteAll(User loginUser) {
        DeleteHistories deleteHistories = new DeleteHistories();
        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(loginUser));
        }
        return deleteHistories;
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(answers);
    }
}
