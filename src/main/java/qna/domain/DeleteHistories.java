package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {
    private List<DeleteHistory> deleteHistories;

    public DeleteHistories() {
        this.deleteHistories = new ArrayList<>();
    }

    public void add(DeleteHistory deleteHistory) {
        deleteHistories.add(deleteHistory);
    }

    public void addAll(List<DeleteHistory> deleteHistories) {
        this.deleteHistories.addAll(deleteHistories);
    }

    public List<DeleteHistory> get() {
        return deleteHistories;
    }

    public int size() {
        return deleteHistories.size();
    }
}
