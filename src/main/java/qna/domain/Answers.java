package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import qna.exception.CannotDeleteException;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private final List<Answer> values;

    protected Answers() {
        this(new ArrayList<>());
    }

    protected Answers(List<Answer> values) {
        this.values = copy(values);
    }

    public static Answers from(Answers answers) {
        return from(answers.values);
    }

    public static Answers from(List<Answer> answers) {
        return new Answers(answers);
    }

    private static List<Answer> copy(List<Answer> answers) {
        return answers.stream()
                .map(Answer::from)
                .collect(Collectors.toList());
    }

    public void add(Answer answer) {
        if (Objects.isNull(answer)) {
            return;
        }
        values.add(answer);
    }

    public DeleteHistories deleteAll(User user) throws CannotDeleteException {
        DeleteHistories deleteHistories = new DeleteHistories();
        for (Answer answer : values) {
            deleteHistories.add(answer.delete(user));
        }
        return deleteHistories;
    }

    public int count() {
        return values.size();
    }
}
