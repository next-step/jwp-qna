package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeleteHistories {

    private final List<DeleteHistory> histories;

    public DeleteHistories(List<DeleteHistory> histories) {
        this.histories = histories;
    }

    public DeleteHistories() {
        this.histories = new ArrayList<>();
    }

    public void addDeleteHistory(DeleteHistory deleteHistory) {
        this.histories.add(deleteHistory);
    }

    public List<DeleteHistory> toUnmodifiableList() {
        return histories;
    }

    public DeleteHistories concat(DeleteHistories other) {
        ArrayList<DeleteHistory> concatList = new ArrayList<>();
        concatList.addAll(other.histories);
        return new DeleteHistories(concatList);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistories that = (DeleteHistories) o;
        return Objects.equals(histories, that.histories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(histories);
    }
}
