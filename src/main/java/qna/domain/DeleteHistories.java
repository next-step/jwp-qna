package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteHistories {
    private final List<DeleteHistory> deleteHistoryList;

    public DeleteHistories(List<DeleteHistory> deleteHistoryList) {
        this.deleteHistoryList = deleteHistoryList;
    }

    public DeleteHistories() {
        this(new ArrayList<>());
    }

    public void add(DeleteHistory deleteHistory) {
        deleteHistoryList.add(deleteHistory);
    }

    public DeleteHistories addAll(DeleteHistories deleteHistories) {
        deleteHistoryList.addAll(deleteHistories.getDeleteHistoryList());
        return this;
    }

    public List<DeleteHistory> getDeleteHistoryList() {
        return Collections.unmodifiableList(deleteHistoryList);
    }

}
