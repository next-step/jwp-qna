package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import qna.CannotDeleteException;

public class AnswersOfQuestion {
    private final List<Answer> answers;

    public AnswersOfQuestion(List<Answer> answers){
        this.answers = answers;
    }

    public List<DeleteHistory> generateDeleteHistories(User loginUser) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(loginUser));
        }

        return new ArrayList<>(deleteHistories);
    }
}
