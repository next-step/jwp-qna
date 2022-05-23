package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import qna.CannotDeleteException;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private final List<Answer> answers = new ArrayList<>();

    protected Answers() {}

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public void removeAnswer(Answer answer) {
        this.answers.remove(answer);
    }

    public boolean hasAnswer(Answer answer) {
        return this.answers.contains(answer);
    }

    public void delete(User loginUser) throws CannotDeleteException {
        List<Answer> deletedFalseAnswers = answers.stream().filter(answer -> !answer.isDeleted()).collect(Collectors.toList());
        for (Answer answer : deletedFalseAnswers) {
            answer.delete(loginUser);
        }
    }
}
