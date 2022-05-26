package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {
    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories() {
        this(new ArrayList<>());
    }

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    public void add(DeleteHistory deleteHistory) {
        this.deleteHistories.add(deleteHistory);
    }

    public void merge(DeleteHistories deleteHistories) {
        this.deleteHistories.addAll(deleteHistories.getDeleteHistories());
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }
}
