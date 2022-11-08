package qna.domain.answer;

import java.time.LocalDateTime;
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

    public List<DeleteHistory> deleteAll(User loginUser, LocalDateTime now) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(loginUser, now));
        }
        return deleteHistories;
    }

}
