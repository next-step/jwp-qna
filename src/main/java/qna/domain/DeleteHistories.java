package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {
    private List<DeleteHistory> list;

    public DeleteHistories() {
        list = new ArrayList<>();
    }

    public DeleteHistories(List<DeleteHistory> histories) {
        this.list = histories;
    }

    public List<DeleteHistory> getList() {
        return list;
    }

    public void add(DeleteHistory history) {
        list.add(history);
    }

    public void add(List<DeleteHistory> histories) {
        list.addAll(histories);
    }

    public void contains(DeleteHistory history) {
        list.contains(history);
    }
}
