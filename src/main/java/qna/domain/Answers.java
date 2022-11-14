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

    public Answers() {
        answers = new ArrayList<>();
    }

    public boolean isIdenticalWriter(User loginUser) throws CannotDeleteException {
        for (Answer answer : answers) {
            validateOwner(loginUser, answer);
        }

        return true;
    }

    public void validateOwner(User loginUser, Answer answer) throws CannotDeleteException {
        if (!answer.isOwner(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public int getSize() {
        return answers.size();
    }

    public boolean isEmpty() {
        return answers.isEmpty();
    }
}
