package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DeleteHistories {

    private List<DeleteHistory> deleteHistories = new ArrayList<>();

    protected DeleteHistories() {
    }

    public DeleteHistories(final List<DeleteHistory> deleteHistories) {
        this.deleteHistories = new ArrayList<>(deleteHistories);
    }

    public void addAll(final DeleteHistories other) {
        deleteHistories.addAll(new ArrayList<>(other.deleteHistories));
    }

    public void add(final DeleteHistory deleteHistory) {
        deleteHistories.add(deleteHistory);
    }

    public List<DeleteHistory> getValues() {
        return Collections.unmodifiableList(deleteHistories);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DeleteHistories that = (DeleteHistories) o;
        return Objects.equals(deleteHistories, that.deleteHistories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deleteHistories);
    }
}
