package qna.domain;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Stream.concat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeleteHistories {
    private final List<DeleteHistory> items;

    private DeleteHistories(final List<DeleteHistory> items) {
        this.items = new ArrayList<>(items);
    }

    public static DeleteHistories valueOf(final DeleteHistory... histories) {
        return new DeleteHistories(asList(histories));
    }

    public DeleteHistories add(final DeleteHistory deleteHistory) {
        return new DeleteHistories(concat(items.stream(), Stream.of(deleteHistory)).collect(Collectors.toList()));
    }

    public DeleteHistories addAll(final DeleteHistories deleteHistories) {
        return new DeleteHistories(concatList(deleteHistories));
    }

    private List<DeleteHistory> concatList(final DeleteHistories deleteHistories) {
        return concat(items.stream(), deleteHistories.items.stream()).collect(Collectors.toList());
    }

    public List<DeleteHistory> getItems() {
        return unmodifiableList(items);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeleteHistories)) {
            return false;
        }
        final DeleteHistories that = (DeleteHistories) o;
        return Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }

    @Override
    public String toString() {
        return "DeleteHistories{" +
                "list=" + items +
                '}';
    }
}
