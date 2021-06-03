package qna.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DeleteHistories {

    private final List<DeleteHistory> histories;

    public DeleteHistories(List<DeleteHistory> histories) {
        this.histories = Collections.unmodifiableList(histories);
    }

    public DeleteHistories(DeleteHistory... deleteHistory) {
        this(Arrays.asList(deleteHistory));
    }

    public DeleteHistories addAll(DeleteHistories otherDeleteHistories) {
        List<DeleteHistory> deleteHistories = new ArrayList<>(this.histories);
        deleteHistories.addAll(otherDeleteHistories.getHistories());
        return new DeleteHistories(deleteHistories);
    }

    public List<DeleteHistory> getHistories() {
        return histories;
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
        return Objects.equals(histories, that.histories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(histories);
    }
}
