package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeleteHistories {
    private List<DeleteHistory> histories;

    public DeleteHistories() {
        this.histories = new ArrayList<>();
    }

    public DeleteHistories(List<DeleteHistory> histories) {
        this.histories = histories;
    }

    public void addHistory(DeleteHistory deleteHistory) {
        this.histories.add(deleteHistory);
    }

    public DeleteHistories addAll(DeleteHistories deleteHistories) {
        List<DeleteHistory> deletions = new ArrayList<>(this.histories);
        deletions.addAll(deleteHistories.getHistories());
        return new DeleteHistories(deletions);
    }

    public List<DeleteHistory> getHistories() {
        return new ArrayList<>(histories);
    }

    public int size() {
        return this.histories.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeleteHistories)) return false;
        DeleteHistories that = (DeleteHistories) o;
        return Objects.equals(getHistories(), that.getHistories());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHistories());
    }

    @Override
    public String toString() {
        return "DeleteHistories{" +
                "histories=" + histories +
                '}';
    }
}
