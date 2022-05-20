package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import static java.util.Objects.requireNonNull;

public class DeleteHistories {

    private final List<DeleteHistory> value;

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.value = requireNonNull(deleteHistories, "deleteHistories");
    }

    public DeleteHistories(DeleteHistory deleteHistory) {
        requireNonNull(deleteHistory, "deleteHistories");
        this.value = Collections.singletonList(deleteHistory);
    }

    public DeleteHistories merge(DeleteHistories deleteHistories) {
        requireNonNull(deleteHistories, "deleteHistories");
        final int totalSize = this.value.size() + deleteHistories.value.size();
        final List<DeleteHistory> allDeleteHistories = new ArrayList<>(totalSize);
        allDeleteHistories.addAll(this.value);
        allDeleteHistories.addAll(deleteHistories.value);
        return new DeleteHistories(allDeleteHistories);
    }

    List<DeleteHistory> get() {
        return Collections.unmodifiableList(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistories that = (DeleteHistories) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
