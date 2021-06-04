package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteHistories {

    private List<DeleteHistory> deleteHistories = new ArrayList<>();

    public void addDeleteHistory(DeleteHistory deleteHistory) {
        deleteHistories.add(deleteHistory);
    }

    public void addDeleteHistories(DeleteHistories deleteHistories) {
        this.deleteHistories.addAll(deleteHistories.deleteHistories());
    }

    public List<DeleteHistory> deleteHistories() {
        return Collections.unmodifiableList(deleteHistories);
    }
}
