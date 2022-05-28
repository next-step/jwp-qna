package qna.domain;

import java.util.*;

public class DeleteHistories {
    private final Set<DeleteHistory> deleteHistories;

    public DeleteHistories(final List<DeleteHistory> deleteHistories) {
        if (Objects.isNull(deleteHistories)) {
            throw new IllegalArgumentException("invalid parameter");
        }
        this.deleteHistories= new HashSet<>(deleteHistories);
    }

    public void add(final DeleteHistory deleteHistory) {
        this.deleteHistories.add(deleteHistory);
    }

    public int size() {
        return deleteHistories.size();
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
