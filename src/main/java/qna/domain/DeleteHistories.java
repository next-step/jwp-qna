package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteHistories {
    List<DeleteHistory> deleteHistories;

    public DeleteHistories(Question question, User loginUser) {
        LocalDateTime now = LocalDateTime.now();
        deleteHistories = new ArrayList<>();
        deleteHistories.add(DeleteHistory.of(question, loginUser, now));
        List<Answer> unmodifiableAnswers = question.getUnmodifiableAnswers();
        for (Answer answer : unmodifiableAnswers) {
            deleteHistories.add(DeleteHistory.of(answer, loginUser, now));
        }
    }

    public static DeleteHistories of(Question question, User loginUser) {
        return new DeleteHistories(question, loginUser);
    }

    public List<DeleteHistory> getDeleteHistories() {
        return Collections.unmodifiableList(deleteHistories);
    }
}
