package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {
    private List<DeleteHistory> deleteHistories = new ArrayList<>();

    protected DeleteHistories() {
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }

    public void add(DeleteHistory deleteHistory) {
        deleteHistories.add(deleteHistory);
    }

    public void addAll(List<DeleteHistory> list) {
        deleteHistories.addAll(list);
    }
}
