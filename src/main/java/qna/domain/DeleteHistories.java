package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DeleteHistories {
    private final List<DeleteHistory> values;

    public DeleteHistories() {
        this(new ArrayList<>());
    }

    public DeleteHistories(List<DeleteHistory> values) {
        this.values = copy(values);
    }

    public static DeleteHistories from(List<DeleteHistory> deleteHistories) {
        return new DeleteHistories(deleteHistories);
    }

    private static List<DeleteHistory> copy(List<DeleteHistory> deleteHistories) {
        return deleteHistories.stream()
                .map(DeleteHistory::from)
                .collect(Collectors.toList());
    }

    public List<DeleteHistory> get() {
        return Collections.unmodifiableList(values);
    }

    public void add(DeleteHistory value) {
        values.add(value);
    }

    public void addAll(DeleteHistories deleteHistories) {
        values.addAll(deleteHistories.values);
    }

    public int count() {
        return values.size();
    }

    @Override
    public String toString() {
        return "DeleteHistories{" +
                "values=" + values +
                '}';
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
        return Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }
}
