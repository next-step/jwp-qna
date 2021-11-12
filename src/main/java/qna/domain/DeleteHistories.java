package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DeleteHistories {

    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories(DeleteHistories answerDeleteHistories, DeleteHistory questionDeleteHistory) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(questionDeleteHistory);
        deleteHistories.addAll(answerDeleteHistories.deleteHistories);
        this.deleteHistories = Collections.unmodifiableList(deleteHistories);
    }

    public DeleteHistories(List<DeleteHistory> collect) {
        deleteHistories = Collections.unmodifiableList(collect);
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
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
