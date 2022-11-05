package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DeleteHistories {

    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = new ArrayList<>(deleteHistories);
    }

    public DeleteHistories(DeleteHistory deleteHistory) {
        this.deleteHistories = Collections.singletonList(deleteHistory);
    }

    public List<DeleteHistory> unmodifiedDeleteHistories() {
        return Collections.unmodifiableList(deleteHistories);
    }

    public DeleteHistories merge(DeleteHistories deleteHistories) {
        List<DeleteHistory> newDeleteHistories = new ArrayList<>(this.deleteHistories);
        newDeleteHistories.addAll(deleteHistories.deleteHistories);
        return new DeleteHistories(newDeleteHistories);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeleteHistories that = (DeleteHistories) o;
        return Objects.equals(deleteHistories, that.deleteHistories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deleteHistories);
    }
}
