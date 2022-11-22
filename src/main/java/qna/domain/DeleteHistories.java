package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DeleteHistories {
    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories() {
        this.deleteHistories = new ArrayList<>();
    }

    public DeleteHistories(DeleteHistory deleteHistory) {
        this.deleteHistories = new ArrayList<>();
        addDeleteHistory(deleteHistory);
    }

    public void addDeleteHistory(DeleteHistory deleteHistory) {
        this.deleteHistories.add(deleteHistory);
    }

    public void addDeleteHistory(DeleteHistories deleteHistories) {
        for (DeleteHistory deleteHistory : deleteHistories.deleteHistories) {
            this.deleteHistories.add(deleteHistory);
        }
    }

    public List<DeleteHistory> unmodifiedDeleteHistories() {
        return Collections.unmodifiableList(this.deleteHistories);
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
