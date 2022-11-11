package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.OneToMany;
import qna.CannotDeleteException;

public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    void add(Answer answer) {
        this.answers.add(answer);
    }

    public DeleteHistories deleteAll(User loginUser) throws CannotDeleteException {
        DeleteHistories deleteHistories = new DeleteHistories();
        for (Answer answer : answers) {
            validateWriterAndLoginUser(loginUser, answer);
            deleteHistories.add(DeleteHistory.ofAnswer(answer.getId(), answer.getWriter()));
            answer.delete();
        }
        return deleteHistories;
    }

    private void validateWriterAndLoginUser(User loginUser, Answer answer) throws CannotDeleteException {
        if (!answer.isOwner(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

}
