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

    public List<DeleteHistory> unmodifiedDeleteHistories() {
        return Collections.unmodifiableList(this.deleteHistories);
    }

    public void add(DeleteHistory deleteHistory) {
        this.deleteHistories.add(deleteHistory);
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
        if(deleteHistories.size() != that.deleteHistories.size()) {
            return false;
        }
        return deleteHistories.containsAll(that.deleteHistories) && that.deleteHistories.containsAll(deleteHistories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deleteHistories);
    }
}
