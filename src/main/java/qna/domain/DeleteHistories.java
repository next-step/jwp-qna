package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteHistories {

    private final List<DeleteHistory> deleteHistoryItems;

    public DeleteHistories() {
        this.deleteHistoryItems = new ArrayList<>();
    }

    public DeleteHistories(List<DeleteHistory> DeleteHistories) {
        this.deleteHistoryItems = new ArrayList<>(DeleteHistories);
    }

    public void add(DeleteHistory deleteHistory) {
        if(deleteHistoryItems.contains(deleteHistory)) {
            return;
        }
        this.deleteHistoryItems.add(deleteHistory);
    }

    public void addAll(DeleteHistories deleteHistories) {
        deleteHistories.deleteHistoryItems.forEach(this::add);
    }

    public int size() {
        return this.deleteHistoryItems.size();
    }

    public List<DeleteHistory> getAll() {
        return Collections.unmodifiableList(new ArrayList<>(this.deleteHistoryItems));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeleteHistories that = (DeleteHistories) o;

        return deleteHistoryItems.equals(that.deleteHistoryItems);
    }

    @Override
    public int hashCode() {
        return deleteHistoryItems.hashCode();
    }
}
