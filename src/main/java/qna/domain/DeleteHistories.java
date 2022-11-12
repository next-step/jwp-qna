package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {
    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    public DeleteHistories(DeleteHistory deleteHistory) {
        this.deleteHistories = new ArrayList<>();
        deleteHistories.add(deleteHistory);
    }

    public void add(DeleteHistory deleteHistory) {
        deleteHistories.add(deleteHistory);
    }

    public boolean contains(DeleteHistory deleteHistory) {
        return deleteHistories.contains(deleteHistory);
    }

    public List<DeleteHistory> getList() {
        return deleteHistories;
    }

    public void addAll(DeleteHistories delete) {
        deleteHistories.addAll(delete.getList());
    }
}
