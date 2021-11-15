package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {

    List<DeleteHistory> list;

    public DeleteHistories() {
        this.list = new ArrayList<>();
    }

    public DeleteHistories(List<DeleteHistory> list) {
        this.list = list;
    }

    public List<DeleteHistory> getList() {
        return list;
    }

    public void add(DeleteHistory deleteHistory) {
        list.add(deleteHistory);
    }

    public void addAll(DeleteHistories deleteHistories) {
        list.addAll(deleteHistories.getList());
    }

    public int size() {
        return list.size();
    }

}
