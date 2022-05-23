package qna.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public List<DeleteHistory> delete(User loginUser) {
        return answers.stream()
                .filter(answer -> !answer.isDeleted())
                .map(answer -> answer.delete(loginUser))
                .collect(Collectors.toList());
    }
}
