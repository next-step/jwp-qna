package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {
    List<DeleteHistory> deleteHistories;

    public DeleteHistories() {
        deleteHistories = new ArrayList<>();
    }

    public void addDeleteHistory(DeleteHistory deleteHistory) {
        deleteHistories.add(deleteHistory);
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }

    public void addDeleteHistories(DeleteHistories delete) {
        for (DeleteHistory history: delete.getDeleteHistories()) {
            deleteHistories.add(history);
        }
    }
}
