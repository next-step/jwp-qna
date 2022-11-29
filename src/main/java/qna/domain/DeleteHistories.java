package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeleteHistories {

    private List<DeleteHistory> deleteHistories;

    public DeleteHistories() {
    }

    public void add(DeleteHistory deleteHistory) {
        if (deleteHistories == null) {
            deleteHistories = new ArrayList<>();
        }
        this.deleteHistories.add(deleteHistory);
    }

    public void add(DeleteHistories deleteHistories) {
        if (this.deleteHistories == null) {
            this.deleteHistories = new ArrayList<>();
        }
        this.deleteHistories.addAll(deleteHistories.getDeleteHistories());
    }

    public void add(List<DeleteHistory> deleteHistories) {
        if (this.deleteHistories == null) {
            this.deleteHistories = new ArrayList<>();
        }
        this.deleteHistories.addAll(deleteHistories);
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }

    public void setDeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
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
