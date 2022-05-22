package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {
    private final List<DeleteHistory> histories;

    public DeleteHistories() {
        this.histories = new ArrayList<>();
    }

    public void add(DeleteHistory deleteHistory) {
        this.histories.add(deleteHistory);
    }

    public void addAll(DeleteHistories dleteHistorys) {
        this.histories.addAll(dleteHistorys.histories);
    }

    public List<DeleteHistory> getHistories() {
        return histories;
    }
}
