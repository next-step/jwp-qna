package qna.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import qna.CannotDeleteException;
import qna.ForbiddenException;

@Embeddable
public class Answers {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "answer_id")
    private final List<Answer> answers;

    protected Answers() {
        this.answers = new ArrayList<>();
    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public void add(Answer answer) {
        validateDuplicate(answer);
        this.answers.add(answer);
    }

    private void validateDuplicate(Answer answer) {
        if (answers.contains(answer)) {
            throw new ForbiddenException("중복된 답변은 입력할 수 없습니다.");
        }
    }

    public DeleteHistories delete(User loginUser) throws CannotDeleteException {
        DeleteHistories deleteHistories = new DeleteHistories();
        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(loginUser));
        }
        return deleteHistories;
    }

}
