package qna.domain;

import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    @ReadOnlyProperty
    private final List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public DeleteHistories deleteAll(User loginUser) {
        return answers.stream()
                .map(answer -> answer.delete(loginUser))
                .collect(Collectors.collectingAndThen(Collectors.toList(), DeleteHistories::new));
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(answers);
    }
}
