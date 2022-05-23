package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {

    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories() {
        this.deleteHistories = new ArrayList<>();
    }

    public DeleteHistories(DeleteHistory param) {
        this.deleteHistories = new ArrayList<>();
        deleteHistories.add(param);
    }

    public DeleteHistories(List<DeleteHistory> param) {
        this.deleteHistories = param;
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }

    public void add(DeleteHistory deleteHistory) {
        this.deleteHistories.add(deleteHistory);
    }

    public void addAll(List<DeleteHistory> collection) {
        this.deleteHistories.addAll(collection);
    }
}
