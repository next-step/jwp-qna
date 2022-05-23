package qna.question.domain;

import qna.question.exception.CannotDeleteException;
import qna.user.domain.User;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Answers {
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @JoinColumn(name = "question_id")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {}

    public void add(Answer answer) {
        if (!this.answers.contains(answer)) {
            this.answers.add(answer);
        }
    }

    public List<DeleteHistory> deleteAll(User loginId, LocalDateTime deletedAt) throws CannotDeleteException {
        List<DeleteHistory> result = new ArrayList<>();

        for (Answer answer : this.answers) {
            answer.delete(loginId);
            result.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), loginId, deletedAt));
        }

        return result;
    }
}
