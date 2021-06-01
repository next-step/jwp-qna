package qna.domain.wrapper;

import qna.domain.Answer;
import qna.domain.DeleteHistory;
import qna.domain.User;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    public Answers() {}

    public void add(Answer answer) {
        if (!answers.contains(answer)) {
            this.answers.add(answer);
        }
    }

    public List<DeleteHistory> deleteAndReturnDeleteHistories(User loginUser) {
        return answers.stream()
                .map(answer -> answer.deleteAndReturnDeleteHistory(loginUser))
                .collect(toList());
    }
}
