package qna.domain.answer;

import java.util.ArrayList;
import java.util.List;
import qna.CannotDeleteException;
import qna.domain.history.DeleteHistory;
import qna.domain.user.User;

public class Answers {

    List<Answer> answers;

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<DeleteHistory> deleteAll(User loginUser) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            // TODO:: 2deps 리팩토링
            if (!answer.isOwner(loginUser)) {
                throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
            }
            deleteHistories.add(answer.delete(loginUser));
        }
        return deleteHistories;
    }

}
