package qna.domain;

import java.util.*;

public class DeleteHistories {
    private List<DeleteHistory> deleteHistories;

    public DeleteHistories(DeleteHistory ...deleteHistories) {
        this(Arrays.asList(deleteHistories));
    }

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = new ArrayList<>(deleteHistories);
    }

    public DeleteHistories addAll(DeleteHistories deleteAll) {
        List<DeleteHistory> newDeleteHistories = new ArrayList<>(this.deleteHistories);
        newDeleteHistories.addAll(deleteAll.deleteHistories);

        return new DeleteHistories(newDeleteHistories);
    }

    public List<DeleteHistory> toCollection() {
        return Collections.unmodifiableList(deleteHistories);
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
