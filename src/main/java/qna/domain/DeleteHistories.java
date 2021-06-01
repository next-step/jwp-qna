package qna.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DeleteHistories {

    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = Collections.unmodifiableList(deleteHistories);
    }

    public DeleteHistories(DeleteHistory... deleteHistory) {
        this(Arrays.asList(deleteHistory));
    }

    public DeleteHistories addAll(DeleteHistories otherDeleteHistories) {
        List<DeleteHistory> deleteHistories = new ArrayList<>(this.deleteHistories);
        deleteHistories.addAll(otherDeleteHistories.getDeleteHistories());
        return new DeleteHistories(deleteHistories);
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
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
