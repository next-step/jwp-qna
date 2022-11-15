package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteHistories {

    List<DeleteHistory> deleteHistories;

    public DeleteHistories () {
        deleteHistories = new ArrayList<>();
    }

    public void add(DeleteHistory deleteHistory) {
        deleteHistories.add(deleteHistory);
    }

    public void addAll(DeleteHistories deleteHistories) {
        for (DeleteHistory deleteHistory : deleteHistories.getDeletedHistories()) {
            this.deleteHistories.add(deleteHistory);
        }
    }

    public List<DeleteHistory> getDeletedHistories() {
        return Collections.unmodifiableList(deleteHistories);
    }

}
