package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {

    private List<DeleteHistory> deleteHistories = new ArrayList<>();

    public DeleteHistories(DeleteHistory deleteHistories) {
        this.deleteHistories.add(deleteHistories);
    }

    public void add(final List<DeleteHistory> values) {
        deleteHistories.addAll(values);
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }
}
