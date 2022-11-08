package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {

    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public void remove(Answer answer) {
        answers.remove(answer);
    }

    public boolean contains(Answer answer) {
        return answers.contains(answer);
    }

    public boolean isEmpty() {
        return answers.isEmpty();
    }

    public DeleteHistories delete(User loginUser) {
        return new DeleteHistories(answers.stream()
                .map(answer -> answer.delete(loginUser))
                .collect(Collectors.toList()));
    }
}
