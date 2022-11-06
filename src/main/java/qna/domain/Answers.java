package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private List<Answer> answers;

    protected Answers() {
        this.answers = new ArrayList<>();
    }

    public Answers(List<Answer> answers) {
        this.answers = new ArrayList<>(answers);
    }

    public DeleteHistories deleteAll(User questionWriter) throws CannotDeleteException {
        DeleteHistories deleteHistories = new DeleteHistories(new ArrayList<>());
        for (Answer answer : answers) {
            if (!answer.isOwner(questionWriter)) {
                throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
            }
            deleteHistories.add(answer.delete());
        }
        return deleteHistories;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public void removeAnswer(Answer answer) {
        answers.remove(answer);
    }

    public boolean contains(Answer answer) {
        return answers.contains(answer);
    }
}
