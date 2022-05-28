package qna.domain;

import static java.util.Arrays.asList;
import static java.util.stream.Stream.concat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeleteHistories {
    public static final DeleteHistories EMPTY = new DeleteHistories(new ArrayList<>());
    private final List<DeleteHistory> list;

    private DeleteHistories(final List<DeleteHistory> list) {
        this.list = list;
    }

    public static DeleteHistories valueOf(final DeleteHistory... histories) {
        return new DeleteHistories(asList(histories));
    }

    public static DeleteHistories valueOf(final List<DeleteHistory> histories) {
        return new DeleteHistories(histories);
    }

    public DeleteHistories add(final DeleteHistory deleteHistory) {
        return new DeleteHistories(concat(list.stream(), Stream.of(deleteHistory)).collect(Collectors.toList()));
    }

    public DeleteHistories addAll(final DeleteHistories deleteHistories) {
        return new DeleteHistories(concatList(deleteHistories));
    }

    private List<DeleteHistory> concatList(final DeleteHistories deleteHistories) {
        return concat(list.stream(), deleteHistories.list.stream()).collect(Collectors.toList());
    }

    public List<DeleteHistory> getList() {
        return list;
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
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }

    @Override
    public String toString() {
        return "DeleteHistories{" +
                "list=" + list +
                '}';
    }
}
