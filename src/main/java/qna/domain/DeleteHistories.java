package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {

    private final List<DeleteHistory> histories = new ArrayList<>();

    public void add(DeleteHistory deleteHistory) {
        this.histories.add(deleteHistory);
    }

    public void add(DeleteHistories deleteHistories) {
        this.histories.addAll(deleteHistories.getAll());
    }

    public List<DeleteHistory> getAll() {
        return histories;
    }
}
