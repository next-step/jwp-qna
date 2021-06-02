package qna.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import qna.CannotDeleteException;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private final List<Answer> answers = new ArrayList<>();

    public Answers() {}

    public int size() {
        return answers.size();
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public void checkLoginUserAuth(User loginUser) throws CannotDeleteException {
        for (Answer answer : answers) {
            checkUserEqual(answer, loginUser);
        }
    }

    private void checkUserEqual(Answer answer, User loginUser) throws CannotDeleteException {
        if (!answer.isOwner(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }
}
