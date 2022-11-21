package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteHistories {
    List<DeleteHistory> deleteHistories = new ArrayList<>();

    public DeleteHistories() {

    }

    public void add(DeleteHistory deleteHistory) {
        if (!this.deleteHistories.contains(deleteHistory)) {
            this.deleteHistories.add(deleteHistory);
        }
    }

    public void addAll(DeleteHistories deleteHistories) {
        for (DeleteHistory deleteHistory : deleteHistories.values()) {
            this.deleteHistories.add(deleteHistory);
        }
    }

    public List<DeleteHistory> values() {
        return Collections.unmodifiableList(deleteHistories);
    }

}
