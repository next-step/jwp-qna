package qna.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class DeleteHistories {
    private final Set<DeleteHistory> deleteHistories = new HashSet<>();

    public DeleteHistories() {
    }

    public DeleteHistories(final List<DeleteHistory> deleteHistories) {
        if (Objects.isNull(deleteHistories)) {
            throw new IllegalArgumentException("invalid parameter");
        }
        this.deleteHistories.addAll(deleteHistories);
    }

    public void add(final DeleteHistory deleteHistory) {
        this.deleteHistories.add(deleteHistory);
    }

    public int size() {
        return deleteHistories.size();
    }

    public Set<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }

    public boolean isContains(final DeleteHistory deleteHistory) {
        return this.deleteHistories.contains(deleteHistory);
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
