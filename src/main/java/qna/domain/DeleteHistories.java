package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteHistories {
    List<DeleteHistory> deleteHistories;

    public DeleteHistories(Question question, User loginUser) {
        deleteHistories = new ArrayList<>();
        deleteHistories.add(DeleteHistory.of(question, loginUser));
        List<Answer> unmodifiableAnswers = question.getUnmodifiableAnswers();
        for (Answer answer : unmodifiableAnswers) {
            deleteHistories.add(DeleteHistory.of(answer, loginUser));
        }
    }

    public static DeleteHistories of(Question question, User loginUser) {
        return new DeleteHistories(question, loginUser);
    }

    public List<DeleteHistory> getUnmodifiableDeleteHistories() {
        return Collections.unmodifiableList(deleteHistories);
    }
}
