package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import qna.domain.exception.IllegalDeleteHistoriesException;
import qna.domain.exception.IllegalDeleteHistoryException;

public class DeleteHistories {

    private final List<DeleteHistory> deleteHistories;

    private DeleteHistories(final List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    public static DeleteHistories from(final List<DeleteHistory> deleteHistories) {
        validateDeleteHistoriesNotNullOrEmpty(deleteHistories);

        return new DeleteHistories(deleteHistories);
    }

    public static DeleteHistories of(final DeleteHistory deleteHistory,
        final DeleteHistories deleteHistories) {

        validateDeleteHistoryNotNull(deleteHistory);

        List<DeleteHistory> histories = new ArrayList<>();
        histories.add(deleteHistory);
        histories.addAll(deleteHistories.deleteHistories());

        return new DeleteHistories(histories);
    }

    private static void validateDeleteHistoryNotNull(DeleteHistory deleteHistory) {
        if (deleteHistory == null) {
            throw new IllegalDeleteHistoryException();
        }
    }

    private static void validateDeleteHistoriesNotNullOrEmpty(List<DeleteHistory> deleteHistories) {
        if (deleteHistories == null) {
            throw new IllegalDeleteHistoriesException();
        }
    }

    public List<DeleteHistory> deleteHistories() {
        return Collections.unmodifiableList(deleteHistories);
    }

    public int size() {
        return deleteHistories.size();
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
