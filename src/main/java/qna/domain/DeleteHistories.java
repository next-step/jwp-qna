package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {

    private final List<DeleteHistory> deleteHistories = new ArrayList<>();

    public void add(DeleteHistory deleteHistory) {
        deleteHistories.add(deleteHistory);
    }

    public void add(DeleteHistories deleteHistories) {
        this.deleteHistories.addAll(deleteHistories.deleteHistories);
    }

    public List<DeleteHistory> get() {
        return deleteHistories;
    }
}
