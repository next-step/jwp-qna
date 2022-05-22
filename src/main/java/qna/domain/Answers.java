package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private final List<Answer> answers;

    public Answers() {
        this.answers = new ArrayList<>();
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public List<DeleteHistory> deleteAll(User loginUser) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : getNotDeletedAnswers()) {
            deleteHistories.add(answer.delete(loginUser));
        }
        return deleteHistories;
    }

    private List<Answer> getNotDeletedAnswers() {
        return this.answers.stream()
                .filter(Answer::isNotDeleted)
                .collect(Collectors.toList());
    }
}
