package qna.domain.content;

import qna.domain.history.DeleteHistory;
import qna.domain.User;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {}

    List<DeleteHistory> deleteAnswers(User loginUser) {
        return createDeleteHistories(loginUser);
    }

    private List<DeleteHistory> createDeleteHistories(User loginUser) {
        return answers.stream()
                .filter((answer) -> !answer.isDeleted())
                .map((answer) -> answer.delete(loginUser))
                .collect(Collectors.toList());
    }

    void addAnswer(Answer answer, Question parent) {
        if(!hasAnswer(answer)) {
            this.answers.add(answer);
            answer.updateQuestion(parent);
        }
    }

    boolean hasAnswer(Answer answer) {
        return this.answers.contains(answer);
    }
}
