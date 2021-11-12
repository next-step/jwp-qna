package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistoryList {
    private List<DeleteHistory> deleteHistories;

    public DeleteHistoryList() {
        deleteHistories = new ArrayList<>();
    }

    public DeleteHistoryList(DeleteHistory deleteHistory) {
        this.deleteHistories = new ArrayList<>();
        this.deleteHistories.add(deleteHistory);
    }

    public void add(DeleteHistory deleteHistory) {
        deleteHistories.add(deleteHistory);
    }

    public int size() {
        return deleteHistories.size();
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }
}
