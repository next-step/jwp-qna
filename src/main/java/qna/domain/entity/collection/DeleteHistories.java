package qna.domain.entity.collection;

import qna.domain.entity.DeleteHistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteHistories {

    private final List<DeleteHistory> deleteHistories = new ArrayList<>();

    public List<DeleteHistory> list() {
        return Collections.unmodifiableList(this.deleteHistories);
    }

    public int size() {
        return deleteHistories.size();
    }

    public DeleteHistories append(final DeleteHistory deleteHistory) {
        this.deleteHistories.add(deleteHistory);

        return this;
    }

    public DeleteHistories append(final DeleteHistories deleteHistories) {
        this.deleteHistories.addAll(deleteHistories.list());

        return this;
    }
}
