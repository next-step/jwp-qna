package qna.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import qna.exceptions.CannotDeleteException;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public void add(Answer answer) {
        this.answers.add(answer);
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    protected boolean isRemovable(User writer) {
        boolean removable = true;

        for (Answer answer : answers) {
            removable &= answer.isDeleted() | answer.isOwner(writer);
        }

        return removable;
    }

    public DeleteHistories delete(User writer) throws CannotDeleteException {
        if (!isRemovable(writer)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }

        DeleteHistories deleteHistories = new DeleteHistories();
        answers.stream()
            .filter(answer -> !answer.isDeleted())
            .forEach(answer -> {
                answer.delete();
                deleteHistories.add(new DeleteHistory(answer, writer));
            });
        return deleteHistories;
    }

}
