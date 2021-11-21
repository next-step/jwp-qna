package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {
    private List<DeleteHistory> deleteHistories;

    public DeleteHistories() {
        this.deleteHistories = new ArrayList<>();
    }

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    public DeleteHistories(DeleteHistory of) {
        this.deleteHistories = new ArrayList<>();
        deleteHistories.add(of);
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }

    public void add(DeleteHistory deleteHistory) {
        deleteHistories.add(deleteHistory);
    }

    public DeleteHistories merge(DeleteHistories deleteHistories) {
        for(DeleteHistory deleteHistory : deleteHistories.getDeleteHistories()) {
            this.deleteHistories.add(deleteHistory);
        }
        return this;
    }
}