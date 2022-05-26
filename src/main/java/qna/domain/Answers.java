package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private final List<Answer> answers = new ArrayList<>();

    public List<DeleteHistory> delete(User writer) {
        return answers.stream()
            .map(answer -> answer.delete(writer))
            .collect(Collectors.toList());
    }

    public void add(Answer answer) {
        this.answers.add(answer);
    }

    public List<Answer> getAnswers() {
        return answers;
    }
}
