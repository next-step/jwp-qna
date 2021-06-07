package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class DeleteHistories {

    @OneToMany(mappedBy = "user")
    List<DeleteHistory> deletedHistories = new ArrayList<>();

    public DeleteHistories() {
    }

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deletedHistories = deleteHistories;
    }

    public List<DeleteHistory> getDeletedHistories() {
        return deletedHistories;
    }

    public void setDeletedHistories(List<DeleteHistory> deletedHistories) {
        this.deletedHistories = deletedHistories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistories that = (DeleteHistories) o;
        return Objects.equals(deletedHistories, that.deletedHistories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deletedHistories);
    }

    public void addDeleteHistory(DeleteHistory deleteHistory) {
        this.deletedHistories.add(deleteHistory);
    }

}
