package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DeleteHistories {

    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories() {
        this(new ArrayList<>());
    }

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = new ArrayList<>(deleteHistories);
    }

    public static DeleteHistories of(Question question) {
        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.addQuestionDeleteHistory(question);
        return deleteHistories;
    }

    private void addQuestionDeleteHistory(Question question) {
        deleteHistories.add(DeleteHistory.ofQuestion(question));
    }

    public List<DeleteHistory> getDeleteHistories() {
        return Collections.unmodifiableList(new ArrayList<>(this.deleteHistories));
    }

    public void addAll(DeleteHistories otherDeleteHistories) {
        this.deleteHistories.addAll(otherDeleteHistories.deleteHistories);
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
