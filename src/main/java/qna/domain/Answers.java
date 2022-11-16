package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    protected Answers() { }

    public Answers(List<Answer> answer) {
        this.answers = answer;
    }

    public List<Answer> get() {
        return answers;
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public DeleteHistories deleteAll(User loginUser) throws CannotDeleteException {
        DeleteHistories deleteHistories = new DeleteHistories();
        for (Answer answer :  generateAliveAnswers()) {
            DeleteHistory deleteHistory = answer.delete(loginUser);
            deleteHistories.add(deleteHistory);
        }
        return deleteHistories;
    }

    private List<Answer> generateAliveAnswers() {
        return answers.stream()
                .filter(answer -> !answer.isDeleted())
                .collect(Collectors.toList());
    }
}
