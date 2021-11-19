package qna.domain;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DeleteHistories {
    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = Collections.unmodifiableList(deleteHistories);
    }

    public List<DeleteHistory> getValue() {
        return deleteHistories;
    }

    public DeleteHistories create(DeleteHistory deleteHistory) {
        List<DeleteHistory> deleteHistories = new LinkedList<>();
        deleteHistories.addAll(this.deleteHistories);
        deleteHistories.add(deleteHistory);
        return new DeleteHistories(deleteHistories);
    }
}
