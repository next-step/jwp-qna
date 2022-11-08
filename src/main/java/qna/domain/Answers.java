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

    public boolean isEmpty() {
        return answers.isEmpty();
    }

    public void addAnswer(Answer answer) {
        if (isNotContains(answer)) {
            answers.add(answer);
        }
    }

    private boolean isNotContains(Answer answer) {
        return !answers.contains(answer);
    }

    public void remove(Answer answer) {
        answers.remove(answer);
    }

    public DeleteHistories delete(User loginUser) {
        return DeleteHistories.of(answers.stream()
                .filter(Answer::isNotDeleted)
                .map(answer -> answer.delete(loginUser))
                .collect(Collectors.toList()));
    }

    public List<Answer> getAnswers() {
        return answers;
    }
}
