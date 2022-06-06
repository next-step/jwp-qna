package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class DeleteHistories {
    private List<DeleteHistory> deleteHistories = new ArrayList<>();

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    protected DeleteHistories() {
    }

    public void add(DeleteHistory deleteHistory) {
        this.deleteHistories.add(deleteHistory);
    }

    public void addAllDeleteHistories(DeleteHistories deleteHistories) {
        IntStream.range(0, deleteHistories.size()).forEach(i -> this.deleteHistories.add(deleteHistories.get(i)));
    }

    public int size() {
        return deleteHistories.size();
    }

    public DeleteHistory get(int index) {
        return this.deleteHistories.get(index);
    }

    public List<DeleteHistory> toList() {
        return this.deleteHistories;
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
