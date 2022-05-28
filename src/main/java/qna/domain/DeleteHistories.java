package qna.domain;

import java.util.*;
import java.util.function.Predicate;

public class DeleteHistories {
    private final Set<DeleteHistory> deleteHistories = new HashSet<>();

    public DeleteHistories() {
    }

    public DeleteHistories(final List<DeleteHistory> deleteHistories) {
        if (Objects.isNull(deleteHistories)) {
            throw new IllegalArgumentException("invalid parameter");
        }
        this.deleteHistories.addAll(deleteHistories);
    }

    private static Predicate<DeleteHistory> getAnswerPredicate(final Answer answer) {
        return deleteHistory -> Objects.equals(answer.getId(), deleteHistory.getContentId()) &&
                Objects.equals(answer.getWriter(), deleteHistory.getDeletedByUser()) &&
                Objects.equals(deleteHistory.getContentType(), ContentType.ANSWER);
    }

    public void add(final DeleteHistory deleteHistory) {
        this.deleteHistories.add(deleteHistory);
    }

    public int size() {
        return deleteHistories.size();
    }

    public Set<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }

    public Optional<DeleteHistory> findBy(final Answer answer) {
        return this.deleteHistories.stream()
                .filter(deleteHistory -> deleteHistory.isDeletedBy(getAnswerPredicate(answer)))
                .findAny();
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
