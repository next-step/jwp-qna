package qna.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import qna.CannotDeleteException;

@Embeddable
public class Answers {


    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();


    protected Answers() {

    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public boolean add(Answer answer) {
        return answers.add(answer);
    }

    public boolean remove(Answer answer) {
        return answers.remove(answer);
    }

    public boolean contains(Answer answer) {
        return answers.contains(answer);
    }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        if (answers.stream().anyMatch(answer -> !answer.isOwner(loginUser))) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }

        answers.forEach(answer -> answer.deleteAnswer());
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        answers.forEach(answer -> deleteHistories.add(DeleteHistory.createAnswerDeleteHistory(answer)));
        return deleteHistories;
    }
}
