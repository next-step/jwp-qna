package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteHistories {
    List<DeleteHistory> deleteHistories;

    public DeleteHistories(Question question, List<Answer> deletedAnswers, User loginUser) {
        LocalDateTime now = LocalDateTime.now();
        deleteHistories = new ArrayList<>();
        deleteHistories.add(DeleteHistory.of(question, loginUser, now));
        for (Answer answer : deletedAnswers) {
            deleteHistories.add(DeleteHistory.of(answer, loginUser, now));
        }
    }

    public static DeleteHistories of(Question question, List<Answer> deletedAnswers, User loginUser) {
        return new DeleteHistories(question, deletedAnswers, loginUser);
    }

    public List<DeleteHistory> getDeleteHistories() {
        return Collections.unmodifiableList(deleteHistories);
    }
}
