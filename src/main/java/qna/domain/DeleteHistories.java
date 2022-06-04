package qna.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DeleteHistories {
    private final List<DeleteHistory> histories;

    public static DeleteHistories toHistories(final List<DeleteHistory> histories) {
        return new DeleteHistories(Collections.unmodifiableList(histories));
    }

    private DeleteHistories(final List<DeleteHistory> histories) {
        this.histories = histories;
    }

    public List<DeleteHistory> getDeleteHistories() {
        return histories;
    }

    @Override
    public String toString() {
        return "DeleteHistories{" +
                "histories=" + histories +
                '}';
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
