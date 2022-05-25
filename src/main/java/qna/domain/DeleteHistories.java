package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteHistories {
    List<DeleteHistory> deleteHistories;

    public DeleteHistories(Question question, Answers deletedAnswers, User user) {
        LocalDateTime now = LocalDateTime.now();
        deleteHistories = new ArrayList<>();
        deleteHistories.add(DeleteHistory.of(question, user, now));
        for (Answer answer : deletedAnswers.getAnswers()) {
            deleteHistories.add(DeleteHistory.of(answer, user, now));
        }
    }

    public static DeleteHistories of(Question question, Answers deletedAnswers, User user) {
        return new DeleteHistories(question, deletedAnswers, user);
    }

    public List<DeleteHistory> getDeleteHistories() {
        return Collections.unmodifiableList(deleteHistories);
    }
}
