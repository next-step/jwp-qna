package qna.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class DeleteHistories implements Iterable<DeleteHistory> {
    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories() {
        this.deleteHistories = new ArrayList<>();
    }

    public void add(DeleteHistory deleteHistory) {
        this.deleteHistories.add(deleteHistory);
    }

    @Override
    public Iterator<DeleteHistory> iterator() {
        return this.deleteHistories.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeleteHistories)) {
            return false;
        }
        DeleteHistories that = (DeleteHistories) o;
        return deleteHistories.size() == that.deleteHistories.size() &&
                deleteHistories.containsAll(that.deleteHistories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deleteHistories);
    }
}
