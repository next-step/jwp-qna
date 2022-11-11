package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteHistories {

    private List<DeleteHistory> deleteHistories = new ArrayList<>();

    protected DeleteHistories() {
    }

    public void add(DeleteHistory deleteHistory) {
        this.deleteHistories.add(deleteHistory);
    }

    public List<DeleteHistory> values() {
        return Collections.unmodifiableList(deleteHistories);
    }

    @Override
    public String toString() {
        return "DeleteHistories{" +
                "deleteHistories=" + deleteHistories +
                '}';
    }
}
