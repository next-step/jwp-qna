package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {

    private final List<DeleteHistory> deleteHistories;
    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    public DeleteHistories() {
        this.deleteHistories = new ArrayList<>();
    }

    public void add(DeleteHistory deleteHistory) {
        deleteHistories.add(deleteHistory);
    }

    public void addAll(List<DeleteHistory> deleteHistoryList) {
        deleteHistories.addAll(deleteHistoryList);
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }
}
