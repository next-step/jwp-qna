package qna.domain;

import java.util.Collections;
import java.util.List;

public class DeleteHistories {
    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    public List<DeleteHistory> addQueue(DeleteHistory deleteHistory) {
        Collections.reverse(deleteHistories);
        deleteHistories.add(deleteHistory);
        Collections.reverse(deleteHistories);
        return deleteHistories;
    }

    public boolean contains(DeleteHistory deleteHistory) {
        return deleteHistories.contains(deleteHistory);
    }
}
