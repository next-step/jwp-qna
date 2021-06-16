package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {

    private final List<DeleteHistory> deleteHistories = new ArrayList<>();

    public DeleteHistories() {
    }

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories.addAll(deleteHistories);
    }

    public void addDeleteHistory(DeleteHistory deleteHistory) {
        deleteHistories.add(deleteHistory);
    }

    public void addDeleteHistories(DeleteHistories deleteHistories) {
        this.deleteHistories.addAll(deleteHistories.deleteHistories());
    }

    public List<DeleteHistory> deleteHistories() {
        return deleteHistories;
    }
}
