package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {}

    public void add(Answer answer) {
        answers.add(answer);
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }

    public void validOwner(User loginUser) throws CannotDeleteException {
        for (Answer answer: answers) {
            answer.validOwner(loginUser);
        }
    }

    public DeleteHistories delete() {
        DeleteHistories deleteHistories = new DeleteHistories();
        for (Answer answer: answers) {
            answer.delete();
            deleteHistories.addDeleteHistory(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
        }
        return deleteHistories;
    }
}
