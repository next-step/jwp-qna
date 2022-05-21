package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    public Answers() {
    }

    Answers(List<Answer> answers) {
        this.answers = answers;
    }

    List<Answer> getAnswers() {
        return answers;
    }

    public List<DeleteHistory> deleteBy(User loginUser) {
        return answers.stream()
                .map(answer -> answer.deleteBy(loginUser))
                .collect(Collectors.toList());
    }
}
