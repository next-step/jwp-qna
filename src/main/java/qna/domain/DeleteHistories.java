package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {

    private List<DeleteHistory> deleteHistories = new ArrayList<>();

    public void add(DeleteHistory deleteHistory) {
        deleteHistories.add(deleteHistory);
    }

    public void addAll(List<DeleteHistory> histories) {
        deleteHistories.addAll(histories);
    }

    public List<DeleteHistory> getList() {
        return deleteHistories;
    }

}
