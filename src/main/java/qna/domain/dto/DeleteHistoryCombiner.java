package qna.domain.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import qna.domain.DeleteHistory;

public class DeleteHistoryCombiner {

    private List<DeleteHistory> deleteHistories = new ArrayList<>();


    public  void add(DeleteHistory deleteHistory) {
        this.deleteHistories.add(deleteHistory);
    }

    public void add(List<DeleteHistory> deleteHistory) {
        this.deleteHistories.addAll(deleteHistory);
    }

    public List<DeleteHistory> getCombiner() {
        return Collections.unmodifiableList(deleteHistories);
    }
}
