package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    List<Answer> answers = new ArrayList<>();

    public void add(Answer answer) {
        this.answers.add(answer);
    }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        validateWriters(loginUser);
        for (Answer answer : this.answers) {
            answer.delete();
        }
        return createDeleteHistories(loginUser);
    }

    private void validateWriters(User loginUser) throws CannotDeleteException {
        for (Answer answer : this.answers) {
            validateWriter(loginUser, answer);
        }
    }

    private static void validateWriter(User loginUser, Answer answer) throws CannotDeleteException {
        if (!answer.isOwner(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    private List<DeleteHistory> createDeleteHistories(User loginUser) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : this.answers) {
            deleteHistories.add(DeleteHistory.ofAnswer(answer.getId(), loginUser));
        }
        return deleteHistories;
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }
}

