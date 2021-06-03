package qna.domain;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class DeleteHistories implements Iterable<DeleteHistory> {

    private final List<DeleteHistory> deleteHistoryList;

    public DeleteHistories(final List<DeleteHistory> deleteHistoryList) {
        this.deleteHistoryList = Collections.unmodifiableList((deleteHistoryList));
    }

    public int size() {
        return deleteHistoryList.size();
    }

    @Override
    public Iterator<DeleteHistory> iterator() {
        return deleteHistoryList.listIterator();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DeleteHistories that = (DeleteHistories)o;
        return Objects.equals(deleteHistoryList, that.deleteHistoryList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deleteHistoryList);
    }
}
