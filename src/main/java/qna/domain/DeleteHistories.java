package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {
    private List<DeleteHistory> deleteHistories = new ArrayList<>();

    public DeleteHistories() { }

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    public DeleteHistories(DeleteHistory deleteHistory) {
        this.deleteHistories.add(deleteHistory);
    }

    public List<DeleteHistory> histories() {
        return this.deleteHistories;
    }

    public void add(DeleteHistory deleteHistory) {
        this.deleteHistories.add(deleteHistory);
    }

    public void add(DeleteHistories deleteHistories) {
        deleteHistories.histories().stream()
                .forEach(deleteHistory -> this.deleteHistories.add(deleteHistory));
    }

    public void addAll(List<DeleteHistory> deleteHistoryList) {
        this.deleteHistories.addAll(deleteHistoryList);
    }
}
