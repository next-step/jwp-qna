package qna.domain.wrapper;

import qna.domain.DeleteHistory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DeleteHistories {

    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories(final List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    public static DeleteHistories from(final List<DeleteHistory> deleteHistories) {
        return new DeleteHistories(deleteHistories);
    }

    public int size() {
        return deleteHistories.size();
    }

    public List<DeleteHistory> toCollection() {
        return Collections.unmodifiableList(deleteHistories);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistories that = (DeleteHistories) o;
        return Objects.equals(deleteHistories, that.deleteHistories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deleteHistories);
    }
}
