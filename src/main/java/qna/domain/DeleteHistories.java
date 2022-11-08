package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteHistories {

    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories() {
        deleteHistories = new ArrayList<>();
    }

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    public static DeleteHistories ofQuestion(Question question) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now()));

        return new DeleteHistories(deleteHistories);
    }

    public void addAll(List<DeleteHistory> deleteHistories) {
        this.deleteHistories.addAll(deleteHistories);
    }

    public List<DeleteHistory> getDeleteHistories() {
        return Collections.unmodifiableList(deleteHistories);
    }
}
